package com.example.mvvm2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mvvm2.databinding.ActivityMainBinding
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.grid.MainGridFragment
import com.example.mvvm2.record.MainRecordFragment
import com.example.mvvm2.today.MainTodayFragment
import com.example.mvvm2.total.MainTotalFragment
import com.example.mvvm2.viewmodel.DetailViewModel
import com.example.mvvm2.viewmodel.TodayViewModel
import com.example.mvvm2.viewmodel.TotalViewModel
import com.example.mvvm2.viewmodel.ViewModelFactory


class MainActivity : AppCompatActivity() {

    companion object{
        val TAG: String = "로그"
        /** 현재 프래그먼트 */
        var nowFragment = "record"
    }


    /** 데이터바인딩 */
    private val binding: ActivityMainBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_main) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** 현재 프래그먼트 출력  @@ 옵저버로 수정 예정 */
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

    private fun setFragment(fragName: String) {
        /** 트렌잭션 */
        val transaction = supportFragmentManager.beginTransaction()
        val frameId = binding.mainFrag.id

        /** 화면 교체 */
        when (fragName) {
            "record" -> transaction.replace(frameId, MainRecordFragment()).commit()
            "today" -> transaction.replace(frameId, MainTodayFragment()).commit()
            "grid" -> transaction.replace(frameId, MainGridFragment()).commit()
            "total" -> transaction.replace(frameId, MainTotalFragment()).commit()
        }
    }
}