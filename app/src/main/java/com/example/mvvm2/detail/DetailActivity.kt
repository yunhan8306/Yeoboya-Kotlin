package com.example.mvvm2.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.R
import com.example.mvvm2.databinding.ActivityDetailBinding
import com.example.mvvm2.databinding.ActivityMainBinding
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        /** no 인텐트값 호출 */
        recordNo = intent.getStringExtra("no")!!.toLong()

        initDetailActivity()

        /** record 삭제  @@ 버튼 옵저버 수정 필요 */
        binding.btnRemove.setOnClickListener {
            Log.d(TAG, "삭제 클릭")
            deleteRecord()
        }

        /** record 수정  @@ 버튼 옵저버 수정 필요 */
        binding.btnUpdate.setOnClickListener {
            Log.d(TAG, "수정 클릭")
            detailUpdate()
        }
    }

    /** init */
    private fun initDetailActivity() {
        initViewModel()
        setObserver()
        getRecord()
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
    }

    private fun getRecord() {
        detailViewModel.getNoData(recordNo)
    }

    private fun deleteRecord() {
        detailViewModel.deleteData(record)
        finish()
    }

    private fun detailUpdate() {
        val intent = Intent(applicationContext,DetailUpdateActivity::class.java)
        intent.putExtra("record", record)
        startActivity(intent)
    }
}