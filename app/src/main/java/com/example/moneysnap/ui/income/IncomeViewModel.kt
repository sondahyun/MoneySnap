package com.example.moneysnap.ui.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.moneysnap.data.entity.Income
import com.example.moneysnap.data.repository.IncomeRepository
import kotlinx.coroutines.launch

class IncomeViewModel(private val incomeRepo: IncomeRepository) : ViewModel() {

    // 특정 날짜의 모든 수익 데이터를 LiveData로 반환
    fun getIncomesByDate(date: String): LiveData<List<Income>> {
        return incomeRepo.getIncomesByDate(date).asLiveData()
    }

    // 특정 날짜의 총 수익 데이터를 LiveData로 반환
    fun getTotalIncomeByDate(date: String): LiveData<Int> {
        return incomeRepo.getTotalIncomeByDate(date).asLiveData()
    }

    // 새로운 수익 추가
    fun insertIncome(income: Income) = viewModelScope.launch {
        incomeRepo.insertIncome(income)
    }

    // 특정 ID의 수익 삭제
    fun deleteIncomeById(id: Int) = viewModelScope.launch {
        incomeRepo.deleteIncomeById(id)
    }

    // 특정 날짜의 모든 수익 삭제
    fun deleteIncomesByDate(date: String) = viewModelScope.launch {
        incomeRepo.deleteIncomesByDate(date)
    }

    // 특정 수익 객체 삭제
    fun deleteIncome(income: Income) = viewModelScope.launch {
        incomeRepo.deleteIncome(income)
    }
}