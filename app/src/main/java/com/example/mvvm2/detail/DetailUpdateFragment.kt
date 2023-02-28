package com.example.mvvm2.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.R
import com.example.mvvm2.databinding.FragmentDetailUpdateBinding
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.total.MainTotalFragment
import com.example.mvvm2.viewmodel.DetailViewModel
import com.example.mvvm2.viewmodel.MainViewModel
import com.example.mvvm2.viewmodel.ViewModelFactory


class DetailUpdateFragment : Fragment() {

    /** viewModel */
    lateinit var detailViewModel: DetailViewModel
    lateinit var mainViewModel: MainViewModel

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory

    /** record */
    lateinit var record: RecordEntity

    /** 바인딩*/
    private lateinit var binding:FragmentDetailUpdateBinding

    /** 어뎁터 */
    lateinit var adapter:ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** init */
        initDetailUpdateFragment()

        record = mainViewModel.selectRecord
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_update, container, false)

        /** 저장 버튼 클릭 */
        binding.btnSave.setOnClickListener {
            record.title = binding.recordTitle.text.toString()
            record.content = binding.recordContent.text.toString()

            detailViewModel.updateData(record)

            parentFragmentManager.beginTransaction()
                .replace(R.id.detail_frag, DetailFragment())
                .commit()

            /** 이미지 수정 필요 */
        }

        /** 데이터 바인딩 출력 */
        with(binding) {
            viewDetail = record
        }

        /** 뷰페이저 출력 */
        adapter = ViewPagerAdapter()
        var uriList = record.uriList.split("^")

        adapter.uriList = uriList
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    fun initDetailUpdateFragment() {
        initViewModel()
//        setObserver()
    }

    private fun initViewModel() {
        viewModelFactory = ViewModelFactory(RecordRepository())
        detailViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[DetailViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]
    }


    /** 변경 예정 */
    private fun setObserver() {
        detailViewModel.isGetNoDataComplete.observe(this) {
            record = it

            Log.d(TAG, "record - $record")

            /** 데이터 바인딩 출력 */
            with(binding) {
                viewDetail = record
            }

            /** 뷰페이저 출력 */
            adapter = ViewPagerAdapter()
            var qq = record.uriList.split("^")

            adapter.uriList = qq
            binding.viewPager.adapter = adapter
            binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        detailViewModel.isUpdateDataComplete.observe(this) {

            /** 데이터 바인딩 출력 */
            with(binding) {
                viewDetail = record
            }
        }

    }

}