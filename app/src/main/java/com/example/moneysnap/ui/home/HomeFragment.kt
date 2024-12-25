package com.example.moneysnap.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneysnap.MoneySnapApplication
import com.example.moneysnap.databinding.FragmentHomeBinding
import com.example.moneysnap.ui.expense.ExpenseViewModel
import com.example.moneysnap.ui.expense.ExpenseViewModelFactory
import com.example.moneysnap.ui.income.IncomeViewModel
import com.example.moneysnap.ui.income.IncomeViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: TransactionAdapter

    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory((requireActivity().application as MoneySnapApplication).incomeRepository)
    }

    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((requireActivity().application as MoneySnapApplication).expenseRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // RecyclerView 설정
        adapter = TransactionAdapter(emptyList())
        binding.todayList.layoutManager = LinearLayoutManager(requireContext())
        binding.todayList.adapter = adapter

        // 오늘 날짜 가져오기
        val todayDate = getTodayDate()

        // 데이터 로드 및 정렬
        loadTransactions(todayDate)

        // 추가 버튼 클릭 이벤트
        binding.addButton.setOnClickListener {
            val intent = Intent(requireContext(), AddTransactionActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun loadTransactions(date: String) {
        // 수익 데이터를 관찰
        incomeViewModel.getIncomesByDate(date).observe(viewLifecycleOwner) { incomes ->
            expenseViewModel.getExpensesByDate(date).observe(viewLifecycleOwner) { expenses ->
                // Log로 데이터 확인
                Log.d("HomeFragment", "Incomes: $incomes")
                Log.d("HomeFragment", "Expenses: $expenses")

                // TransactionItem 리스트 생성 및 날짜 순 정렬
                val transactionItems = (incomes.map { TransactionItem.IncomeItem(it) } +
                        expenses.map { TransactionItem.ExpenseItem(it) })
                    .sortedByDescending { it.date }

                // RecyclerView 데이터 갱신
                adapter = TransactionAdapter(transactionItems)
                binding.todayList.adapter = adapter
            }
        }
    }

    private fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}