package com.example.moneysnap

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.moneysnap.databinding.ActivityMainBinding
import com.example.moneysnap.ui.expense.ExpenseViewModel
import com.example.moneysnap.ui.expense.ExpenseViewModelFactory
import com.example.moneysnap.ui.fragments.CalendarFragment
import com.example.moneysnap.ui.fragments.MapFragment
import com.example.moneysnap.ui.fragments.MoneyFragment
import com.example.moneysnap.ui.fragments.HomeFragment
import com.example.moneysnap.ui.income.IncomeViewModel
import com.example.moneysnap.ui.income.IncomeViewModelFactory

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // IncomeViewModel
    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory((application as MoneySnapApplication).incomeRepository)
    }

    // ExpenseViewModel
    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((application as MoneySnapApplication).expenseRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(binding.root)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        setBottomNavigationView()

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_home
        }
    }

    fun setBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, HomeFragment()).commit()
                    true
                }

                R.id.fragment_calendar -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, CalendarFragment()).commit()
                    true
                }

                R.id.fragment_money -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, MoneyFragment()).commit()
                    true
                }

                R.id.fragment_map -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, MapFragment()).commit()
                    true
                }

                else -> false
            }
        }
    }
}