package com.example.moneysnap.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "expenses")
data class Expense(
    @PrimaryKey (autoGenerate = true)
    val _id: Int,
    val amount: Int, // 지출 금액
    val category: String, // 지출 카테고리 (예: 음식, 교통)
    val date: String, // 지출 날짜
    val description: String? = null, // 지출 상세 설명
    val place: String? = null // 지출처 (상점, 장소 이름)
)
