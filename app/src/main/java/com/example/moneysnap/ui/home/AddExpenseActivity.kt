package com.example.moneysnap.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.moneysnap.MainActivity
import com.example.moneysnap.MoneySnapApplication
import com.example.moneysnap.data.entity.Expense
import com.example.moneysnap.databinding.AddExpenseBinding
import com.example.moneysnap.ui.expense.ExpenseViewModel
import com.example.moneysnap.ui.expense.ExpenseViewModelFactory
import java.util.*

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var binding: AddExpenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel 초기화
        val expenseViewModel: ExpenseViewModel by viewModels {
            ExpenseViewModelFactory((application as MoneySnapApplication).expenseRepository)
        }

        // 카테고리 Spinner 설정
        setupCategorySpinner()

        // 날짜 선택 버튼 이벤트
        binding.datePickerButton.setOnClickListener {
            showDatePicker()
        }

        // 저장 버튼 이벤트
        binding.submit.setOnClickListener {
            saveExpense(expenseViewModel)
        }
    }

    private fun setupCategorySpinner() {
        val categories = listOf("식비", "교통", "쇼핑", "여행", "마트", "카페")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.expenseCategory.adapter = adapter
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
                binding.expenseDate.text = formattedDate
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun saveExpense(expenseViewModel: ExpenseViewModel) {
        val amount = binding.expenseAmount.text.toString().toIntOrNull()
        val place = binding.expensePlace.text.toString()
        val category = binding.expenseCategory.selectedItem?.toString()
        val date = binding.expenseDate.text.toString()
        val description = binding.expenseDescription.text.toString()

        if (amount == null || amount <= 0 || place.isBlank() || category.isNullOrEmpty() || date.isBlank()) {
            Toast.makeText(this, "모든 필드를 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val expense = Expense(
            _id = 0, // Primary key auto-generated
            amount = amount,
            category = category,
            date = date,
            description = description,
            place = place
        )

        // 데이터 저장
        expenseViewModel.insertExpense(expense)

        Toast.makeText(this, "지출이 저장되었습니다.", Toast.LENGTH_SHORT).show()
        Log.d("ExpenseActivity", "지출 저장 완료 - ID: ${expense._id}, 금액: ${expense.amount}, 장소: ${expense.place}, 카테고리: ${expense.category}, 날짜: ${expense.date}, 설명: ${expense.description}")

        clearFields()
        navigateToHomeFragment()
    }

    private fun clearFields() {
        binding.expenseAmount.text.clear()
        binding.expensePlace.text.clear()
        binding.expenseCategory.setSelection(0)
        binding.expenseDate.text = "날짜를 선택하세요"
        binding.expenseDescription.text.clear()
    }

    private fun navigateToHomeFragment() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("navigate_to", "HomeFragment")
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }
}