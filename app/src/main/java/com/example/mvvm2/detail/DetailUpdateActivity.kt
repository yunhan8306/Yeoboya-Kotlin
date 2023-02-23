package com.example.mvvm2.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.R
import com.example.mvvm2.databinding.ActivityDetailUpdateBinding
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.viewmodel.DetailViewModel
import com.example.mvvm2.viewmodel.ViewModelFactory

class DetailUpdateActivity : AppCompatActivity() {

    /** 데이터바인딩 */
    private val binding: ActivityDetailUpdateBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_detail_update) }

    /** viewModel */
    lateinit var detailViewModel: DetailViewModel

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory

    /** record */
    lateinit var record: RecordEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_update)

        /** 인텐트 data 호출 */
        val intent = intent
        record = intent?.getParcelableExtra("record")!!

        Log.d(TAG, "update - record - $record")

        initDetailActivity()

        /** 데이터 바인딩 출력 */
        with(binding) {
            viewDetail = record
        }

        /** record 수정 */
        binding.btnUpdate.setOnClickListener {
            updateRecord()
        }

    }
    /** init */
    private fun initDetailActivity() {
        initViewModel()
        setObserver()
    }


    private fun initViewModel() {
        viewModelFactory = ViewModelFactory(RecordRepository())
        detailViewModel = ViewModelProvider(this, viewModelFactory)[DetailViewModel::class.java]
    }

    private fun setObserver() {
        detailViewModel.isUpdateData.observe(this) {
            record = it
            Log.d(TAG, "setObserver - update - $record")
        }
    }

    private fun updateRecord() {
        record.title = binding.recordTitle.text.toString()
        record.content = binding.recordContent.text.toString()

        /** 이미지 갱신 수정 필요 */

        detailViewModel.updateData(record)
        finish()
    }





}