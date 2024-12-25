package com.example.moneysnap.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneysnap.data.entity.Expense
import com.example.moneysnap.data.entity.Income
import com.example.moneysnap.databinding.TodayListExpenseBinding
import com.example.moneysnap.databinding.TodayListIncomeBinding

sealed class TransactionItem {
    data class IncomeItem(val income: Income) : TransactionItem()
    data class ExpenseItem(val expense: Expense) : TransactionItem()

    val date: String
        get() = when (this) {
            is IncomeItem -> this.income.date
            is ExpenseItem -> this.expense.date
        }
}

class TransactionAdapter(private val items: List<TransactionItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_INCOME = 1
        private const val VIEW_TYPE_EXPENSE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is TransactionItem.IncomeItem -> VIEW_TYPE_INCOME
            is TransactionItem.ExpenseItem -> VIEW_TYPE_EXPENSE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_INCOME -> IncomeViewHolder(
                TodayListIncomeBinding.inflate(inflater, parent, false)
            )
            VIEW_TYPE_EXPENSE -> ExpenseViewHolder(
                TodayListExpenseBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IncomeViewHolder -> holder.bind((items[position] as TransactionItem.IncomeItem).income)
            is ExpenseViewHolder -> holder.bind((items[position] as TransactionItem.ExpenseItem).expense)
        }
    }

    override fun getItemCount() = items.size

    class IncomeViewHolder(private val binding: TodayListIncomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(income: Income) {
            binding.incomeAmount.text = "₩${income.amount}"
            binding.incomePlace.text = "출처: ${income.place}"
            binding.incomeDate.text = "날짜: ${income.date}"
            binding.incomeDescription.text = "설명: ${income.description ?: "없음"}"
        }
    }

    class ExpenseViewHolder(private val binding: TodayListExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: Expense) {
            binding.expenseAmount.text = "₩${expense.amount}"
            binding.expensePlace.text = "장소: ${expense.place}"
            binding.expenseCategory.text = "카테고리: ${expense.category}"
            binding.expenseDate.text = "날짜: ${expense.date}"
            binding.expenseDescription.text = "설명: ${expense.description ?: "없음"}"
        }
    }
}
