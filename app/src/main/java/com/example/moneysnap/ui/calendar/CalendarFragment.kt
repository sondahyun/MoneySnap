package com.example.moneysnap.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneysnap.MoneySnapApplication
import com.example.moneysnap.databinding.FragmentCalendarBinding
import com.example.moneysnap.ui.expense.ExpenseViewModel
import com.example.moneysnap.ui.expense.ExpenseViewModelFactory
import com.example.moneysnap.ui.home.TransactionAdapter
import com.example.moneysnap.ui.home.TransactionItem
import com.example.moneysnap.ui.income.IncomeViewModel
import com.example.moneysnap.ui.income.IncomeViewModelFactory
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener {
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var selectedDate: LocalDate
    private lateinit var transactionAdapter: TransactionAdapter

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
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        selectedDate = LocalDate.now()
        transactionAdapter = TransactionAdapter(emptyList())
        binding.selectedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.selectedRecyclerView.adapter = transactionAdapter

        setMonthView()

        binding.btnPreviousMonth.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            setMonthView()
        }

        binding.btnNextMonth.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            setMonthView()
        }

        return binding.root
    }

    private fun setMonthView() {
        binding.tvMonth.text = monthYearFromDate(selectedDate)
        val daysInMonth = generateDaysInMonthArray(selectedDate)

        val calendarAdapter = CalendarAdapter(
            daysInMonth,
            incomeViewModel,
            expenseViewModel,
            viewLifecycleOwner, // LifecycleOwner 전달
            this
        )
        val layoutManager = GridLayoutManager(requireContext(), 7) // 7열로 설정
        binding.calendarRecyclerView.layoutManager = layoutManager
        binding.calendarRecyclerView.adapter = calendarAdapter
    }

    private fun generateDaysInMonthArray(date: LocalDate): List<LocalDate?> {
        val daysInMonthArray = mutableListOf<LocalDate?>()

        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()

        val firstOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value % 7 // 일요일이 0이 되도록 설정

        for (i in 1 until dayOfWeek) {
            daysInMonthArray.add(null)
        }

        for (day in 1..daysInMonth) {
            daysInMonthArray.add(LocalDate.of(selectedDate.year, selectedDate.month, day))
        }

        while (daysInMonthArray.size % 7 != 0) {
            daysInMonthArray.add(null)
        }

        return daysInMonthArray
    }

    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("M월")
        return date.format(formatter)
    }

    override fun onItemClick(date: LocalDate?) {
        if (date != null) {
            val formattedDate = date.format(DateTimeFormatter.ofPattern("d일 E요일"))
            binding.tvSelectedDay.text = formattedDate

            loadTransactionsForDate(date.toString())
        }
    }

    private fun loadTransactionsForDate(date: String) {
        incomeViewModel.getIncomesByDate(date).observe(viewLifecycleOwner) { incomes ->
            expenseViewModel.getExpensesByDate(date).observe(viewLifecycleOwner) { expenses ->
                val transactionItems = (incomes.map { TransactionItem.IncomeItem(it) } +
                        expenses.map { TransactionItem.ExpenseItem(it) })
                    .sortedByDescending { it.date }

                // RecyclerView에 데이터 설정
                transactionAdapter = TransactionAdapter(transactionItems)
                binding.selectedRecyclerView.adapter = transactionAdapter
            }
        }
    }
}