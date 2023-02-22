package com.example.mvvm2.record

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.R
import com.example.mvvm2.databinding.ActivityMainBinding
import com.example.mvvm2.databinding.FragmentMainRecordBinding
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.viewmodel.RecordViewModel
import com.example.mvvm2.viewmodel.ViewModelFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainRecordFragment : Fragment() {

    /** 데이터바인딩*/
    lateinit var binding: FragmentMainRecordBinding

    /** viewModel */
    lateinit var recordViewModel: RecordViewModel

    /** uri 담을 MutableList*/
    lateinit var uriList: MutableList<String>

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRecordFragment()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_record, container, false)

        /** 이미지 추가(갤러리) */
        binding.imageAdd.setOnClickListener { activityResultLauncher.launch(openGallery()) }

        /** record 저장 */
        binding.btnSave.setOnClickListener { saveRecord() }

        return binding.root
    }

    /** 갤러리 인텐트 */
    private fun openGallery(): Intent {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        return intent
    }

    /** 갤러리 uri 가져오기*/
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
                uriList = mutableListOf()

                result.data?.let { _data ->
                    _data.data?.let {
                        uriList.add(it.toString())
                    } ?: _data?.clipData?.let {
                        for (i in 0 until it.itemCount) {
                            var imgUri = it.getItemAt(i).uri.toString()
                            uriList.add(imgUri)
                        }
                    }
                }
            }
            Log.d(TAG, "uriList -  $uriList")
        }

    fun initRecordFragment() {
        initViewModel()
        setObserver()
    }

    /** viewModel init */
    private fun initViewModel() {
        viewModelFactory = ViewModelFactory(RecordRepository())
        recordViewModel = ViewModelProvider(this, viewModelFactory).get(RecordViewModel::class.java)
    }

    /** observer set */
    private fun setObserver() {
        recordViewModel.isSaveComplete.observe(this) {
            no ->
            Log.d(TAG, "saveData - record no - $no")

            /** 초기화 */
            binding.recordTitle.setText("")
            binding.recordInput.setText("")
            uriList = mutableListOf()
            // 이미지 출력된 이미지 초기화 필요 > 뷰페이저에 들어가는 리스트 초기화로 할 예정

        }
    }

    /** record 저장 */
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveRecord() {
        if(binding.recordInput.toString().trim().isEmpty().not()) {

            /** date, time */
            val current = LocalDateTime.now()

            var title = binding.recordTitle.text.toString()
            var content = binding.recordInput.text.toString()
            var date = current.format(DateTimeFormatter.ISO_DATE)
            var time = current.format(DateTimeFormatter.ISO_TIME).substring(0 until 8)

            /** uriList 확인 @@분기 처리 필요 */
            var uriListStr = if(uriList.isEmpty()){ "" } else { uriList.joinToString(separator = "^") }

            /** title or content 입력 시 저장 */
            if(title != "" || content != ""){
                /** 저장할 RecordEntity */
                var recordEntity = RecordEntity(0,title,content,date,time,uriListStr)
                recordViewModel.saveRecord(recordEntity)
                Log.d(TAG, "recordEntity - $recordEntity")
            }
        }
    }
}