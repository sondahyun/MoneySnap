package com.example.moneysnap.ui.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneysnap.data.network.Exchange
import com.example.moneysnap.data.repository.ExchangeRepository

class ExchangeViewModelFactory(private val exchangeRepository: ExchangeRepository) : ViewModelProvider.Factory {
    // ViewModel 객체를 생성하는 함수를 재정의
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 생성하려는 클래스가 FoodViewModel 일 경우 객체 생성
        if (modelClass.isAssignableFrom(ExchangeViewModel::class.java)) {
            return ExchangeViewModel(exchangeRepository) as T
        }
        return IllegalArgumentException("Unknown ViewModel class") as T
    }

}