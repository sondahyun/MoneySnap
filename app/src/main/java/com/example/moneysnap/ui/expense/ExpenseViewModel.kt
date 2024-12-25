package com.example.moneysnap.ui.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.moneysnap.data.entity.Expense
import com.example.moneysnap.data.repository.ExpenseRepository
import kotlinx.coroutines.launch

class ExpenseViewModel(private val expenseRepo: ExpenseRepository) : ViewModel() {
    // fun getExpensesByDate(date: String): LiveData<List<Expense>> = expenseRepo.getExpensesByDate(date).asLiveData()

    // 해당 하는 날의 모든 지출
    fun getExpensesByDate(date: String): LiveData<List<Expense>> {
        return expenseRepo.getExpensesByDate(date).asLiveData()
    }

    // 해당 하는 날의 총 지출
    fun getTotalExpenseByDate(date: String): LiveData<Int> {
        return expenseRepo.getTotalExpenseByDate(date).asLiveData()
    }

    // 지출 추가
    fun insertExpense(expense: Expense) = viewModelScope.launch {
        expenseRepo.insertExpense(expense)
    }

    // 해당 하는 _id 의 지출 삭제
    fun deleteExpenseById(id: Int) = viewModelScope.launch {
        expenseRepo.deleteExpenseById(id)
    }

    // 해당 하는 date 의 모든 지출 삭제
    fun deleteExpensesByDate(date: String) = viewModelScope.launch {
        expenseRepo.deleteExpensesByDate(date)
    }

    // 특정 지출 객체 삭제
    fun deleteExpense(expense: Expense) = viewModelScope.launch {
        expenseRepo.deleteExpense(expense)
    }
}