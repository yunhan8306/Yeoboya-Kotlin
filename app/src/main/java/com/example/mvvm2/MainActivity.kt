package com.example.mvvm2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.example.mvvm2.databinding.ActivityMainBinding
import com.example.mvvm2.grid.MainGridFragment
import com.example.mvvm2.record.MainRecordFragment
import com.example.mvvm2.today.MainTodayFragment


class MainActivity : AppCompatActivity() {

    companion object{

        val TAG: String = "로그"
        /** 현재 프래그먼트 */
        var nowFragment = "record"

    }

    private val databinding: ActivityMainBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_main) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** 현재 프래그먼트 출력 */
        setFragment(nowFragment)

        databinding.fragRecord.setOnClickListener {
            nowFragment = "record"
            setFragment(nowFragment)
        }

        databinding.fragToday.setOnClickListener {
            nowFragment = "today"
            setFragment(nowFragment)
        }

        databinding.fragGrid.setOnClickListener {
            nowFragment = "grid"
            setFragment(nowFragment)
        }
    }

    private fun setFragment(fragName: String) {
        /** 트렌잭션 */
        val transaction = supportFragmentManager.beginTransaction()
        val frameId = databinding.mainFrag.id

        /** 화면 교체 */
        when (fragName) {
            "record" -> transaction.replace(frameId, MainRecordFragment()).commit()
            "today" -> transaction.replace(frameId, MainTodayFragment()).commit()
            "grid" -> transaction.replace(frameId, MainGridFragment()).commit()
        }
    }
}