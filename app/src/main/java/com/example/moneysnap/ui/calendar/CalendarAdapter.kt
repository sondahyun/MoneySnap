package com.example.moneysnap.ui.calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.moneysnap.R
import com.example.moneysnap.databinding.CalendarDayItemBinding
import com.example.moneysnap.ui.expense.ExpenseViewModel
import com.example.moneysnap.ui.income.IncomeViewModel
import java.time.LocalDate

class CalendarAdapter(
    private val days: List<LocalDate?>,
    private val incomeViewModel: IncomeViewModel,
    private val expenseViewModel: ExpenseViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val onItemListener: OnItemListener
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var selectedPosition: Int = days.indexOf(LocalDate.now()) // 오늘 날짜 기본 선택

    interface OnItemListener {
        fun onItemClick(date: LocalDate?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalendarDayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = days[position]
        holder.bind(date, position == selectedPosition)

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            onItemListener.onItemClick(date)
        }
    }

    override fun getItemCount(): Int = days.size

    inner class CalendarViewHolder(private val binding: CalendarDayItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(date: LocalDate?, isSelected: Boolean) {
            if (date == null) {
                binding.tvCellDay.text = ""
                binding.tvDayPlus.visibility = View.GONE
                binding.tvDayMinus.visibility = View.GONE
                binding.root.setBackgroundResource(0)
                return
            }

            binding.tvCellDay.text = date.dayOfMonth.toString()
            binding.tvDayPlus.visibility = View.VISIBLE
            binding.tvDayMinus.visibility = View.VISIBLE

            binding.root.setBackgroundResource(if (isSelected) R.drawable.bg_selected_date else 0)

            incomeViewModel.getTotalIncomeByDate(date.toString())
                .observe(lifecycleOwner) { totalIncome ->
                    binding.tvDayPlus.text = "+${formatCurrency(totalIncome)}"
                }

            expenseViewModel.getTotalExpenseByDate(date.toString())
                .observe(lifecycleOwner) { totalExpense ->
                    binding.tvDayMinus.text = "-${formatCurrency(totalExpense)}"
                }
        }

        private fun formatCurrency(amount: Int?): String {
            return if (amount != null) {
                String.format("%,d", amount)
            } else {
                "0"
            }
        }
    }
}