package com.example.moneysnap.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneysnap.data.entity.Expense
import com.example.moneysnap.databinding.ExpenseTodayListBinding

class ExpenseTodayListAdapter(
    private var expenses: List<Expense>
) : RecyclerView.Adapter<ExpenseTodayListAdapter.ExpenseViewHolder>() {

    // 뷰 홀더 클래스
    class ExpenseViewHolder(val binding: ExpenseTodayListBinding) : RecyclerView.ViewHolder(binding.root)

    // 클릭 이벤트 리스너 인터페이스 정의
    interface OnExpenseClickListener {
        fun onExpenseClick(view: View, position: Int)
    }

    // 외부에서 클릭 리스너를 설정하기 위한 변수
    private var expenseClickListener: OnExpenseClickListener? = null

    // 클릭 리스너를 설정하는 메서드
    fun setOnExpenseClickListener(listener: OnExpenseClickListener?) {
        expenseClickListener = listener
    }

    // 아이템 개수 반환
    override fun getItemCount(): Int = expenses.size

    // 뷰 홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ExpenseTodayListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    // 뷰 홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.binding.apply {
            place.text = expense.place ?: "Unknown Place" // 장소
            category.text = expense.category // 카테고리
            date.text = expense.date // 날짜
            amount.text = "${expense.amount} 원" // 금액
        }

        // 클릭 이벤트 설정
        holder.binding.root.setOnClickListener {
            expenseClickListener?.onExpenseClick(it, position)
        }
    }

    // 새로운 데이터 설정 (리스트 갱신)
    fun updateExpenses(newExpenses: List<Expense>) {
        expenses = newExpenses
        notifyDataSetChanged()
    }
}