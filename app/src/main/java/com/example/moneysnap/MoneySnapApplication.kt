package com.example.moneysnap

import android.app.Application
import com.example.moneysnap.data.database.AppDatabase
import com.example.moneysnap.data.network.ExchangeService
import com.example.moneysnap.data.repository.ExchangeRepository
import com.example.moneysnap.data.repository.ExpenseRepository
import com.example.moneysnap.data.repository.IncomeRepository

class MoneySnapApplication : Application() {
    // Lazy initialization for database
    val database by lazy {
        AppDatabase.getDatabase(this)
    }

    // Lazy initialization for repositories
    val expenseRepository by lazy {
        ExpenseRepository(database.expenseDao())
    }

    val incomeRepository by lazy {
        IncomeRepository(database.incomeDao())
    }

    val exchangeService by lazy {
        ExchangeService(this)
    }

    val exchangeRepository by lazy {
        ExchangeRepository(exchangeService)
    }


}