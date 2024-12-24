package com.example.moneysnap.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.moneysnap.data.entity.Expense
import com.example.moneysnap.data.entity.Income
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {
    @Insert
    suspend fun insertIncome(income: Income)

    // 해당 하는 날의 모든 수익
    @Query("SELECT * FROM incomes WHERE date = :date")
    fun getIncomesByDate(date: String): Flow<List<Income>>

    // 해당 하는 날의 총 수익
    @Query("SELECT SUM(amount) FROM incomes WHERE date = :date")
    fun getTotalIncomeByDate(date: String): Flow<Double>

    // 해당 하는 _id 의 수익 삭제
    @Query("DELETE FROM incomes WHERE _id = :id")
    suspend fun deleteIncomeById(id: Int)

    // 해당 하는 date 의 모든 수익 삭제
    @Query("DELETE FROM incomes WHERE date = :date")
    suspend fun deleteIncomesByDate(date: String)

    // Delete an entire income object
    @Delete
    suspend fun deleteIncome(income: Income)
}