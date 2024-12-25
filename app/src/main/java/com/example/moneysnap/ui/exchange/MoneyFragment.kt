package com.example.moneysnap.ui.exchange

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneysnap.MoneySnapApplication
import com.example.moneysnap.R
import com.example.moneysnap.databinding.FragmentMoneyBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MoneyFragment : Fragment() {
    private lateinit var binding: FragmentMoneyBinding // 뷰 바인딩 추가
    private lateinit var exchangeAdapter: ExchangeAdapter

    private val exchangeViewModel: ExchangeViewModel by viewModels {
        ExchangeViewModelFactory((requireActivity().application as MoneySnapApplication).exchangeRepository)
    }

    // 기본값: 오늘 날짜, API 형식에 맞춰 `yyyyMMdd`로 설정
    private var selectedDate: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoneyBinding.inflate(inflater, container, false) // 뷰 바인딩 초기화
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기 날짜 설정
        val initialDisplayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        binding.selectedDate.text = initialDisplayDate // 사용자 친화적인 포맷

        Log.d("MoneyFragment", "초기 날짜 설정: $selectedDate (표시용: $initialDisplayDate)")

        // 날짜 선택 버튼 클릭 리스너
        binding.datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        // RecyclerView 초기화
        binding.exchangeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        exchangeAdapter = ExchangeAdapter(emptyList())
        binding.exchangeRecyclerView.adapter = exchangeAdapter

        // 환율 정보 가져오기
        fetchExchangeRates()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)

            // API 요청 형식: yyyyMMdd
            selectedDate = formattedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

            // 사용자에게 표시할 형식: yyyy-MM-dd
            binding.selectedDate.text = formattedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            Log.d("MoneyFragment", "사용자가 선택한 날짜: $selectedDate")

            // 선택된 날짜로 데이터 다시 가져오기
            fetchExchangeRates()
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun fetchExchangeRates() {
        val authKey = getString(R.string.authkey) // strings.xml에서 authkey 가져오기
        val data = "AP01" // 환율 데이터 타입

        Log.d("MoneyFragment", "환율 정보 요청: authKey=$authKey, searchDate=$selectedDate, data=$data")

        exchangeViewModel.getExchanges(authKey, selectedDate, data)

        // LiveData 관찰
        exchangeViewModel.exchanges.observe(viewLifecycleOwner) { exchanges ->
            if (exchanges != null && exchanges.isNotEmpty()) {
                Log.d("MoneyFragment", "환율 정보 불러오기 성공: ${exchanges.size}개의 데이터")
                // RecyclerView 업데이트
                exchangeAdapter.updateData(exchanges)
                binding.exchangeRecyclerView.visibility = View.VISIBLE
            } else {
                Log.e("MoneyFragment", "환율 정보를 불러올 수 없습니다. searchDate=$selectedDate")
                Toast.makeText(requireContext(), "환율 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                binding.exchangeRecyclerView.visibility = View.GONE
            }
        }
    }
}