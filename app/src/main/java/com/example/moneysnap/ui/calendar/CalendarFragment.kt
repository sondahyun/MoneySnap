package com.example.moneysnap.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moneysnap.R
import com.example.moneysnap.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener {
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var selectedDate: LocalDate
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        selectedDate = LocalDate.now()
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

        val calendarAdapter = CalendarAdapter(daysInMonth, this)
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
            // 날짜 포맷팅 (예: "24일 화요일")
            val formattedDate = date.format(DateTimeFormatter.ofPattern("d일 E요일"))
            // TextView에 설정
            binding.tvSelectedDay.text = formattedDate
        }
    }

}