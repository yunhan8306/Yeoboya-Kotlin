package com.example.mvvm2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm2.databinding.ActivityMainBinding
import com.example.mvvm2.detail.DetailFragment
import com.example.mvvm2.detail.DetailUpdateFragment
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.grid.MainGridFragment
import com.example.mvvm2.record.MainRecordFragment
import com.example.mvvm2.today.MainTodayFragment
import com.example.mvvm2.total.MainTotalFragment
import com.example.mvvm2.viewmodel.*


class MainActivity : AppCompatActivity() {

    companion object{
        val TAG: String = "로그"
        /** 현재 프래그먼트 */
        var nowFragment = "record"
    }

    /** viewModel */
    lateinit var totalViewModel: TotalViewModel
    lateinit var detailViewModel: DetailViewModel
    lateinit var recordViewModel: RecordViewModel
    lateinit var todayViewModel: TodayViewModel

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory

    /** 데이터바인딩 */
    private val binding: ActivityMainBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()

        /** 현재 프래그먼트 출력  @@ 바인딩어뎁터 수정 예정 */
        setFragment(nowFragment)

        binding.fragRecord.setOnClickListener {
            nowFragment = "record"
            setFragment(nowFragment)
        }

        binding.fragToday.setOnClickListener {
            nowFragment = "today"
            setFragment(nowFragment)
        }

        binding.fragGrid.setOnClickListener {
            nowFragment = "grid"
            setFragment(nowFragment)
        }

        binding.fragTotal.setOnClickListener {
            nowFragment = "total"
            setFragment(nowFragment)
        }
    }

    fun setFragment(fragName: String) {
        /** 트렌잭션 */
        val transaction = supportFragmentManager.beginTransaction()
        val frameId = binding.mainFrag.id

        /** 화면 교체 */
        when (fragName) {
            "record" -> transaction.replace(frameId, MainRecordFragment()).commit()
            "today" -> transaction.replace(frameId, MainTodayFragment()).commit()
            "grid" -> transaction.replace(frameId, MainGridFragment()).commit()
            "total" -> transaction.replace(frameId, MainTotalFragment()).commit()

            /** 상세보기, 수정 */
//            "view" -> transaction.replace(frameId, DetailFragment()).commit()
//            "update" -> transaction.replace(frameId, DetailUpdateFragment()).commit()
        }
    }

    fun initViewModel() {
        totalViewModel = ViewModelProvider(this, viewModelFactory)[TotalViewModel::class.java]
        detailViewModel = ViewModelProvider(this, viewModelFactory)[DetailViewModel::class.java]
        recordViewModel = ViewModelProvider(this, viewModelFactory)[RecordViewModel::class.java]
        todayViewModel = ViewModelProvider(this, viewModelFactory)[TodayViewModel::class.java]
    }
}