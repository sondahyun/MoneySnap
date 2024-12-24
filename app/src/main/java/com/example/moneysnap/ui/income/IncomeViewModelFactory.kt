package com.example.moneysnap.ui.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneysnap.data.repository.IncomeRepository

class IncomeViewModelFactory (private val incomeRepository: IncomeRepository) : ViewModelProvider.Factory {
    // ViewModel 객체를 생성하는 함수를 재정의
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // ViewModel의 클래스 정보
        if (modelClass.isAssignableFrom(IncomeViewModel::class.java)) {
            // 만들어지는 ViewModel의 형식
            @Suppress("UNCHECKED_CAST")
            return IncomeViewModel(incomeRepository) as T
        }
        return IllegalArgumentException("Unknown ViewModel class") as T
    }

}