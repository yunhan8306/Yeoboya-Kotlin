package com.example.mvvm2.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm2.MainActivity
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.R
import com.example.mvvm2.databinding.FragmentDetailBinding
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.today.MainTodayFragment
import com.example.mvvm2.viewmodel.DetailViewModel
import com.example.mvvm2.viewmodel.TodayViewModel
import com.example.mvvm2.viewmodel.ViewModelFactory


class DetailFragment : Fragment() {

    /** viewModel */
    lateinit var detailViewModel: DetailViewModel

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory

    /** record */
    lateinit var record: RecordEntity

    /** 데이터바인딩*/
    private lateinit var binding: FragmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDetailFragment()

        setFragmentResultListener("no") { requestKey, bundle ->
            val recordNo = bundle.getString("bundleKey")!!.toLong()
            detailViewModel.getNoData(recordNo)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        binding.btnRemove.setOnClickListener {
            detailViewModel.deleteData(record)
        }

        binding.btnUpdate.setOnClickListener {
            setFragmentResult("no2", bundleOf("bundleKey" to record.no.toString()))
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frag, DetailUpdateFragment())
                .commit()
        }
        return binding.root
    }

    fun initDetailFragment() {
        initViewModel()
        setObserver()
    }


    private fun initViewModel() {
        viewModelFactory = ViewModelFactory(RecordRepository())
        detailViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[DetailViewModel::class.java]
    }

    private fun setObserver() {
        detailViewModel.isGetNoDataComplete.observe(this) {
            record = it

            /** 데이터 바인딩 출력 */
            with(binding) {
                viewDetail = record
            }
        }

        detailViewModel.isDeleteDataComplete.observe(this) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frag, MainTodayFragment())
                .commit()
        }
    }

}