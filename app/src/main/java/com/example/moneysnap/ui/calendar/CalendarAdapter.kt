package com.example.moneysnap.ui.calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneysnap.R
import com.example.moneysnap.databinding.CalendarDayItemBinding
import java.time.LocalDate

class CalendarAdapter(
    private val days: List<LocalDate?>,
    private val onItemListener: OnItemListener
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION // 선택된 위치 저장

    interface OnItemListener {
        fun onItemClick(date: LocalDate?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalendarDayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val date = days[position]
        holder.bind(date, position == selectedPosition) // 선택 여부 전달
        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition) // 이전 선택된 아이템 업데이트
            notifyItemChanged(selectedPosition) // 새로 선택된 아이템 업데이트
            onItemListener.onItemClick(date)
        }
    }

    override fun getItemCount(): Int = days.size

    inner class CalendarViewHolder(private val binding: CalendarDayItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(date: LocalDate?, isSelected: Boolean) {
            if (date == null) {
                // 빈 셀: 날짜 텍스트 및 부가 정보 숨김
                binding.tvCellDay.text = ""
                binding.tvDayPlus.visibility = View.GONE
                binding.tvDayMinus.visibility = View.GONE
                binding.root.setBackgroundResource(0) // 배경 초기화
            } else {
                // 유효한 날짜: 텍스트 표시 및 부가 정보 표시
                binding.tvCellDay.text = date.dayOfMonth.toString()
                binding.tvDayPlus.visibility = View.VISIBLE
                binding.tvDayMinus.visibility = View.VISIBLE

                // 선택된 상태에 따라 배경 변경
                if (isSelected) {
                    binding.root.setBackgroundResource(R.drawable.bg_selected_date)
                } else {
                    binding.root.setBackgroundResource(0) // 선택 해제된 상태로 초기화
                }
            }
        }
    }
}
