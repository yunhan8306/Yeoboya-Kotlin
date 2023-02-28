package com.example.mvvm2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm2.databinding.ActivityMainBinding
import com.example.mvvm2.grid.MainGridFragment
import com.example.mvvm2.record.MainRecordFragment
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.today.MainTodayFragment
import com.example.mvvm2.total.MainTotalFragment
import com.example.mvvm2.viewmodel.*


class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG: String = "로그"
    }

    /** viewModel */
    lateinit var mainViewModel: MainViewModel

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory

    /** detailFragment visibility / true - 보이기, false - 숨기기 */
    private var visibilityTF: Boolean? = null

    /** 뒤로가기 버튼 콜백함수 */
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(visibilityTF == true) {
                binding.detailFrag.visibility = View.GONE
            }
        }
    }


    /** 바인딩 */
    private val binding: ActivityMainBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()

        this.onBackPressedDispatcher.addCallback(this, callback)

        setFragment(mainViewModel.agoFragment)

        /** 현재 프래그먼트 출력  @@ 바인딩어뎁터 수정 예정 */
        binding.fragRecord.setOnClickListener {
            setFragment("record")
            mainViewModel.agoFragment = "record"
        }

        binding.fragToday.setOnClickListener {
            setFragment("today")
            mainViewModel.agoFragment = "today"
        }

        binding.fragGrid.setOnClickListener {
            setFragment("grid")
            mainViewModel.agoFragment = "grid"
        }

        binding.fragTotal.setOnClickListener {
            setFragment("total")
            mainViewModel.agoFragment = "total"
        }

        /** detail_frag 옵저버 */
        mainViewModel.visibilityDetailFragment.observe(this) {
            visibilityTF = mainViewModel.visibilityDetailFragment.value

            if(visibilityTF == false) {
                binding.detailFrag.visibility = View.GONE
            } else if(visibilityTF == true) {
                binding.detailFrag.visibility = View.VISIBLE
            }
        }
    }

    /** main_frag 변경 */
    private fun setFragment(nowFragment: String) {
        var now = nowFragment // 현재 프래그먼트
        var ago = mainViewModel.agoFragment // 이전 프래그먼트

        if(nowFragment == "") now = "record" // 초기설정

        if(now != ago) {
            /** 트렌잭션 */
            val transaction = supportFragmentManager.beginTransaction()
            val frameId = binding.mainFrag.id

            /** 화면 교체 */
            when (now) {
                "record" -> transaction.replace(frameId, MainRecordFragment()).commit()
                "today" -> transaction.replace(frameId, MainTodayFragment()).commit()
                "grid" -> transaction.replace(frameId, MainGridFragment()).commit()
                "total" -> transaction.replace(frameId, MainTotalFragment()).commit()
            }
        }
    }

    private fun initViewModel() {
        viewModelFactory = ViewModelFactory(RecordRepository())
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }
}