package com.example.moneysnap.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "incomes")
data class Income(
    @PrimaryKey (autoGenerate = true)
    val _id: Int,
    val amount: Double, // 수입 금액
    val source: String, // 수입 출처 (예: 월급, 보너스, 중고거래)
    val date: String, // 수입 날짜
    val description: String? = null, // 수입 상세 설명
)
