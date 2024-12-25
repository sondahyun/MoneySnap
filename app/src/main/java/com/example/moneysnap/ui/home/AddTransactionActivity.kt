package com.example.moneysnap.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moneysnap.databinding.AddTransactionBinding

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var binding: AddTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 수입 추가 버튼 클릭 시
        binding.addIncome.setOnClickListener {
            val intent = Intent(this, AddIncomeActivity::class.java)
            startActivity(intent)
        }

        // 지출 추가 버튼 클릭 시
        binding.addExpense.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }
    }
}