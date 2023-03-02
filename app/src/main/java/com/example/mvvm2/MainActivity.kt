package com.example.mvvm2

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm2.databinding.ActivityMainBinding
import com.example.mvvm2.grid.MainGridFragment
import com.example.mvvm2.record.MainRecordFragment
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.today.MainTodayFragment
import com.example.mvvm2.total.MainTotalFragment
import com.example.mvvm2.viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG: String = "로그"
    }

    /** viewModel */
//    private val mainViewModel: MainViewModel by viewModels()
    lateinit var mainViewModel: MainViewModel
    lateinit var detailViewModel: DetailViewModel

    /** viewModelFactory */
    private lateinit var viewModelFactory: ViewModelFactory

    /** detailFragment visibility / true - 보이기, false - 숨기기 */
    private var visibilityTF: Boolean? = null

    /** 뒤로가기 플래그 */
    private var doubleBackPressed = false

    /** 뒤로가기 버튼 콜백 */
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.d(TAG, "visibilityTF - $visibilityTF")
            if(visibilityTF == true) {
                binding.detailFrag.visibility = View.GONE
                binding.mainFrag.visibility = View.VISIBLE
                visibilityTF = false
            } else {
                if(doubleBackPressed){
                    finishAffinity()
                    return
                }
                doubleBackPressed = true
                Toast.makeText(this@MainActivity, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000).run {
                        doubleBackPressed = false
                    }
                }
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
                binding.mainFrag.visibility = View.GONE
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

    private fun initMainActivity() {
        initViewModelFactory()
        setObserver()
    }

    private fun initViewModelFactory() {
        viewModelFactory = ViewModelFactory(RecordRepository())
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        detailViewModel = ViewModelProvider(this, viewModelFactory)[DetailViewModel::class.java]
    }

    private fun setObserver() {
        detailViewModel.isDeleteDataComplete.observe(this) {
            Toast.makeText(this,"삭제가 완료되었습니다", Toast.LENGTH_SHORT).show()
        }
        detailViewModel.isUpdateDataComplete.observe(this) {
            Toast.makeText(this,"수정이 완료되었습니다", Toast.LENGTH_SHORT).show()
        }
    }
}