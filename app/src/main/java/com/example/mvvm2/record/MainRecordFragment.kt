package com.example.mvvm2.record

import android.annotation.SuppressLint
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.RequestManager
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.R
import com.example.mvvm2.databinding.FragmentMainRecordBinding
import com.example.mvvm2.detail.ViewPagerAdapter
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.viewmodel.MainViewModel
import com.example.mvvm2.viewmodel.RecordViewModel
import com.example.mvvm2.viewmodel.ViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainRecordFragment : Fragment() {

    /** viewModel */
    private val recordViewModel: RecordViewModel by viewModels { viewModelFactory }

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory

    /** 바인딩*/
    lateinit var binding: FragmentMainRecordBinding

    /** 어뎁터 */
    lateinit var adapter: ViewPagerAdapter

    /** uri 담을 MutableList*/
    private var uriList: MutableList<String> = mutableListOf()

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

        /** 이미지 뷰페이저 출력 */
        printViewPager()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        /** 이미지 갱신 */
        resetViewPagerAdapter()
    }

    private fun initRecordFragment() {
        initViewModelFactory()
        setObserver()
    }

    /** viewModel init */
    private fun initViewModelFactory() {
        viewModelFactory = ViewModelFactory(RecordRepository())
    }

    /** observer set */
    private fun setObserver() {
        recordViewModel.isSaveComplete.observe(this) {
            /** 초기화 */
            binding.recordTitle.setText("")
            binding.recordInput.setText("")
            uriList = mutableListOf()
            resetViewPagerAdapter()
        }
    }

    /** record 저장 */
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveRecord() {
        if(binding.recordInput.toString().trim().isEmpty().not()) {
            /** date, time */
            val current = LocalDateTime.now()

            val title = binding.recordTitle.text.toString()
            val content = binding.recordInput.text.toString()
            val date = current.format(DateTimeFormatter.ISO_DATE)
            val time = current.format(DateTimeFormatter.ISO_TIME).substring(0 until 8)

            /** uriList 확인 */
            val uriListStr = uriList.takeIf { it.isNotEmpty() }?.joinToString(separator = "^") ?: ""

            /** title or content 입력 시 저장 */
            if(title != "" || content != ""){
                /** 저장할 RecordEntity */
                val recordEntity = RecordEntity(0,title,content,date,time,uriListStr)
                recordViewModel.saveRecord(recordEntity)
                Log.d(TAG, "recordEntity - $recordEntity")
            }
        }
    }

    /** 뷰페이저 출력 */
    private fun printViewPager() {
        adapter = ViewPagerAdapter()

        adapter.uriList = uriList
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    /** 뷰페이저 이미지 갱신 */
    @SuppressLint("NotifyDataSetChanged")
    fun resetViewPagerAdapter() {
        adapter.uriList = uriList
        adapter.notifyDataSetChanged()
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
                    } ?: _data.clipData?.let {
                        for (i in 0 until it.itemCount) {
                            val imgUri = it.getItemAt(i).uri.toString()
                            uriList.add(imgUri)
                        }
                    }
                }
            }
            Log.d(TAG, "uriList -  $uriList")
        }
}