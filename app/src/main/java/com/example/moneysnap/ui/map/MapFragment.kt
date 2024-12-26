package com.example.moneysnap.ui.map

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.moneysnap.MoneySnapApplication
import com.example.moneysnap.R
import com.example.moneysnap.data.map.Place
import com.example.moneysnap.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: LatLng? = null


    private val mapViewModel: MapViewModel by viewModels {
        MapViewModelFactory((requireContext().applicationContext as MoneySnapApplication).mapRepository)
    }

    private val TAG = "MapFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        locationRequest = LocationRequest.Builder(3000)
            .setMaxUpdates(1) // 최대 1회 업데이트
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.locations.firstOrNull()
                if (location != null) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation!!, 15F))
                    Log.d(TAG, "Current location: Lat: ${location.latitude}, Lng: ${location.longitude}")
                } else {
                    Log.d(TAG, "LocationResult is null.")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        // 지도 초기화
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // 현재 위치 버튼
        binding.btnCurrentLocation.setOnClickListener {
            if (checkPermissions()) {
                startLocationRequest()
            } else {
                Toast.makeText(requireContext(), "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }


        // 검색 버튼
        binding.btnSearchNearby.setOnClickListener {
            val query = binding.search.text.toString().ifEmpty { "ATM" }
            fetchPlacesAndDisplay(query)
        }

        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        Log.d(TAG, "GoogleMap is ready")
    }

    private fun fetchPlacesAndDisplay(query: String) {
        val clientId = getString(R.string.naver_client_id)
        val clientSecret = getString(R.string.naver_client_secret)

        mapViewModel.getPlaces(clientId, clientSecret, query, 5, 1, "random")

        mapViewModel.places.observe(viewLifecycleOwner) { places ->
            if (places != null && places.isNotEmpty()) {
                Log.d(TAG, "Fetched places: ${places.size}")
                displayMarkers(places)
            } else {
                Log.d(TAG, "No places found.")
                Toast.makeText(requireContext(), "$query 결과를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayMarkers(places: List<Place>) {
        if (!::googleMap.isInitialized) {
            Log.e(TAG, "GoogleMap is not initialized.")
            return
        }

        googleMap.clear()
        Log.d(TAG, "Displaying ${places.size} places on the map.")

        for (place in places) {
            try {
                val location = LatLng(place.mapY.toDouble() / 1e7, place.mapX.toDouble() / 1e7) // Divide by 1e7 for proper scaling
                val markerOptions = MarkerOptions()
                    .position(location)
                    .title(place.title)
                    .snippet(place.address)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

                googleMap.addMarker(markerOptions)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 14F))
            } catch (e: Exception) {
                Log.e(TAG, "Error adding marker for place: ${place.title}", e)
            }
        }
    }

    private fun startLocationRequest() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(ACCESS_FINE_LOCATION, false) -> {
                Log.d(TAG, "Fine location permission granted.")
                startLocationRequest()
            }
            permissions.getOrDefault(ACCESS_COARSE_LOCATION, false) -> {
                Log.d(TAG, "Coarse location permission granted.")
                startLocationRequest()
            }
            else -> {
                Log.d(TAG, "Location permission denied.")
                Toast.makeText(requireContext(), "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            locationPermissionRequest.launch(
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            )
            false
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}