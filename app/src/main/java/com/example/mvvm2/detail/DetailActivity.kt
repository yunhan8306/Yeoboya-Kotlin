package com.example.mvvm2.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm2.MainActivity
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.R
import com.example.mvvm2.databinding.ActivityDetailBinding
import com.example.mvvm2.databinding.ActivityMainBinding
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.today.MainTodayFragment
import com.example.mvvm2.viewmodel.DetailViewModel
import com.example.mvvm2.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    /** 데이터바인딩 */
    private val binding: ActivityDetailBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_detail) }

    /** viewModel */
    lateinit var detailViewModel: DetailViewModel

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory

    /** record */
    lateinit var record: RecordEntity

    /** record no */
    var recordNo: Long = 0

    /** activityResultLauncher */
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        /** no 인텐트값 호출 */

        val intent = intent
        record = intent?.getParcelableExtra("record")!!

        /** 데이터 출력 */
        with(binding) {
            viewDetail = record
        }

        initDetailActivity()

        /** record 삭제  @@ 버튼 옵저버 수정 필요 */
        binding.btnRemove.setOnClickListener {
            Log.d(TAG, "삭제 클릭")
            deleteRecord()
        }

        /** record 수정  @@ 버튼 옵저버 수정 필요 */
        binding.btnUpdate.setOnClickListener {
            Log.d(TAG, "수정 클릭")
            detailActivityIntent()
        }
    }

    override fun onStart() {
        super.onStart()
        updateRecord()
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(applicationContext,MainTodayFragment::class.java)
        Log.d(TAG, "onDestroy - record - $record")
        intent.putExtra("record", record)
        setResult(RESULT_OK, intent)

        if (!isFinishing) finish()

        updateRecord()
    }

    /** init */
    private fun initDetailActivity() {
        initViewModel()
//        setObserver()
//        getRecord()
    }

    private fun initViewModel() {
        viewModelFactory = ViewModelFactory(RecordRepository())
        detailViewModel = ViewModelProvider(this, viewModelFactory)[DetailViewModel::class.java]
    }

    private fun setObserver() {
        detailViewModel.isGetNoDataComplete.observe(this) {
            record = it
            Log.d(TAG, "setObserver - record - $record")

            /** 데이터 바인딩 출력 */
            with(binding) {
                viewDetail = record
            }
        }

        detailViewModel.isUpdateDataComplete.observe(this) {
            record = it

            /** 데이터 바인딩 출력 */
            with(binding) {
                viewDetail = record
            }
        }
    }

    private fun getRecord() {
        detailViewModel.getNoData(recordNo)
    }

    private fun deleteRecord() {
        detailViewModel.deleteData(record)
        finish()
    }

    private fun detailActivityIntent() {
        val intent = Intent(applicationContext,DetailUpdateActivity::class.java)
        intent.putExtra("record", record)
        activityResultLauncher.launch(intent)
    }

    private fun updateRecord() {
        /** 인텐트 data 호출  @@ getParcelableExtra 수정 필요 */
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
                val intent: Intent? = it.data
                record = intent?.getParcelableExtra("updateRecord")!!

                /** 데이터 바인딩 출력 */
                with(binding) {
                    viewDetail = record
                }
            }
        }
    }
}