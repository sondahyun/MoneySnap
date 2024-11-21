package ddwu.com.mobileapp.week05.archictectureref

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ddwu.com.mobileapp.week05.archictectureref.databinding.ActivityMainBinding
import ddwu.com.mobileapp.week05.archictectureref.ui.RefViewModel
import ddwu.com.mobileapp.week05.archictectureref.ui.RefViewModelFactory

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
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

        val refViewModel : RefViewModel by viewModels {
            RefViewModelFactory( (application as RefApplication).refRepository )
        }

        // 지속 관찰
        refViewModel.allRefs.observe(this, Observer {
            refEntities ->
            Log.d(TAG, refEntities.toString())
        })

        // RefViewModel 의 findName(id: Int) 호출 후 One-shot 결과 확인
        refViewModel.nameData.observe(this, Observer { findName ->
            // 화면 요소 등에 출력
            Log.d (TAG, findName)
            binding.textView.setText(findName)
        })

        binding.button.setOnClickListener {
            refViewModel.findName(1)
        }

    }
}