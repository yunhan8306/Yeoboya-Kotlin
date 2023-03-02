package com.example.mvvm2

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
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
    private val mainViewModel: MainViewModel by viewModels(factoryProducer = { viewModelFactory })

    /** viewModelFactory */
    private lateinit var viewModelFactory: ViewModelFactory

    /** detailFragment visibility / true - 보이기, false - 숨기기 */
    private var visibilityTF: Boolean? = null

    /** 뒤로가기 버튼 콜백함수 */
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(visibilityTF == true) {
                binding.detailFrag.visibility = View.GONE
                binding.mainFrag.visibility = View.VISIBLE
            }
        }
    }

    /** 바인딩 */
    private val binding: ActivityMainBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_main) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModelFactory()

        this.onBackPressedDispatcher.addCallback(this, callback)

        setFragment(mainViewModel.agoFragment)

        /** 현재 프래그먼트 출력  @@ 바인딩어뎁터 수정 예정 */
        binding.fragRecord.setOnClickListener { setFragment("record") }

        binding.fragToday.setOnClickListener { setFragment("today") }

        binding.fragGrid.setOnClickListener { setFragment("grid") }

        binding.fragTotal.setOnClickListener { setFragment("total") }

        /** detail_frag 옵저버 */
        mainViewModel.visibilityDetailFragment.observe(this) {
            visibilityTF = mainViewModel.visibilityDetailFragment.value

            if(visibilityTF == false) {
                binding.detailFrag.visibility = View.GONE
                binding.mainFrag.visibility = View.VISIBLE
            } else if(visibilityTF == true) {
                binding.detailFrag.visibility = View.VISIBLE
                binding.detailFrag.isClickable = true
                binding.mainFrag.visibility = View.INVISIBLE
            }
        }
    }

    /**
     * main_frag
     * 현재 프래그먼트를 입력받아 이전 프래그먼트와 비교 후 다를 경우 교체
     * 변경 */
    @RequiresApi(Build.VERSION_CODES.O)
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
        mainViewModel.agoFragment = nowFragment
        mainViewModel.setVisibilityDetailFragment(false)
    }

    private fun initViewModelFactory() {
        viewModelFactory = ViewModelFactory(RecordRepository())
    }
}