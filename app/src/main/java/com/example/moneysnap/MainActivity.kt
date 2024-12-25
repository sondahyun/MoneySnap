package com.example.moneysnap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moneysnap.databinding.ActivityMainBinding
import com.example.moneysnap.ui.home.HomeFragment
import com.example.moneysnap.ui.calendar.CalendarFragment
import com.example.moneysnap.ui.map.MapFragment
import com.example.moneysnap.ui.exchange.MoneyFragment

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navigateTo = intent.getStringExtra("navigate_to")

        if (navigateTo == "HomeFragment") {
            showHomeFragment()
        } else if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_home
            showHomeFragment()
        }

        setBottomNavigationView()
    }

    private fun showHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, HomeFragment())
            .commit()
    }

    private fun setBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_home -> {
                    showHomeFragment()
                    true
                }

                R.id.fragment_calendar -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, CalendarFragment())
                        .commit()
                    true
                }

                R.id.fragment_money -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, MoneyFragment())
                        .commit()
                    true
                }

                R.id.fragment_map -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, MapFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }
}