package com.example.moneysnap.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moneysnap.data.dao.ExpenseDao
import com.example.moneysnap.data.dao.IncomeDao
import com.example.moneysnap.data.entity.Expense
import com.example.moneysnap.data.entity.Income
import com.example.moneysnap.data.repository.ExpenseRepository
import java.time.Instant

@Database (
    entities = [Expense::class, Income::class], // 등록된 entity
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao() : ExpenseDao
    abstract fun incomeDao() : IncomeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // 싱글톤 인스턴스 제공
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "money_db" // Database 이름
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}