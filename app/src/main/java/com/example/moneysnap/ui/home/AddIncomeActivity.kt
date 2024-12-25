package com.example.moneysnap.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.moneysnap.MainActivity
import com.example.moneysnap.MoneySnapApplication
import com.example.moneysnap.data.entity.Income
import com.example.moneysnap.databinding.AddIncomeBinding
import com.example.moneysnap.ui.income.IncomeViewModel
import com.example.moneysnap.ui.income.IncomeViewModelFactory
import java.util.*

class AddIncomeActivity : AppCompatActivity() {
    private lateinit var binding: AddIncomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val incomeViewModel: IncomeViewModel by viewModels {
            IncomeViewModelFactory((application as MoneySnapApplication).incomeRepository)
        }

        // Date picker button click event
        binding.datePickerButton.setOnClickListener {
            showDatePicker()
        }

        // 확인 버튼 클릭 이벤트
        binding.submit.setOnClickListener {
            val amountText = binding.incomeAmount.text.toString()
            val place = binding.incomePlace.text.toString()
            val date = binding.incomeDate.text.toString()
            val description = binding.incomeDescription.text.toString()

            if (amountText.isNotEmpty() && place.isNotEmpty() && date != "날짜를 선택하세요") {
                val amount = amountText.toIntOrNull()
                if (amount == null || amount <= 0) {
                    Toast.makeText(this, "유효한 금액을 입력하세요.", Toast.LENGTH_SHORT).show()
                } else {
                    val income = Income(
                        _id = 0, // Primary key auto-generated
                        amount = amount,
                        place = place,
                        date = date,
                        description = description
                    )
                    incomeViewModel.insertIncome(income)
                    Toast.makeText(this, "수입이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("IncomeActivity", "수입 저장 완료 - ID: ${income._id}, 금액: ${income.amount}, 장소: ${income.place}, 날짜: ${income.date}, 설명: ${income.description}")
                    clearFields()

                    navigateToHomeFragment()
                }
            } else {
                Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = android.app.DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                binding.incomeDate.text = formattedDate
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun clearFields() {
        binding.incomeAmount.text.clear()
        binding.incomePlace.text.clear()
        binding.incomeDate.text = "날짜를 선택하세요"
        binding.incomeDescription.text.clear()
    }

    private fun navigateToHomeFragment() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("navigate_to", "HomeFragment") // 전달할 데이터
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }
}