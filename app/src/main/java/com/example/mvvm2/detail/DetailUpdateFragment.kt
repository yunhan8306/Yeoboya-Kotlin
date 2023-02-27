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
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.R
import com.example.mvvm2.databinding.FragmentDetailUpdateBinding
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.viewmodel.DetailViewModel
import com.example.mvvm2.viewmodel.ViewModelFactory


class DetailUpdateFragment : Fragment() {

    /** viewModel */
    lateinit var detailViewModel: DetailViewModel

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory

    /** record */
    lateinit var record: RecordEntity

    /** 데이터바인딩*/
    private lateinit var binding:FragmentDetailUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "DetailUpdateFragment - onCreate called")

        initDetailUpdateFragment()

        setFragmentResultListener("no2") { requestKey, bundle ->
            val recordNo = bundle.getString("bundleKey")!!.toLong()

            Log.d(TAG, "recordNo - $recordNo")
            detailViewModel.getNoData(recordNo)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_update, container, false)

        binding.btnSave.setOnClickListener {

            record.title = binding.recordTitle.text.toString()
            record.content = binding.recordContent.text.toString()

            detailViewModel.updateData(record)

            Log.d(TAG, "DetailUpdateFragment - onCreateView called")

            setFragmentResult("no", bundleOf("bundleKey" to record.no.toString()))
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frag, DetailFragment())
                .commit()
        }

        return binding.root
    }

    fun initDetailUpdateFragment() {
        initViewModel()
        setObserver()
    }

    private fun initViewModel() {
        viewModelFactory = ViewModelFactory(RecordRepository())
        detailViewModel = ViewModelProvider(this, viewModelFactory)[DetailViewModel::class.java]
    }

    private fun setObserver() {
        detailViewModel.isGetNoDataComplete.observe(this) {
            record = it

            Log.d(TAG, "record - $record")

            /** 데이터 바인딩 출력 */
            with(binding) {
                viewDetail = record
            }
        }

        detailViewModel.isUpdateDataComplete.observe(this) {

            /** 데이터 바인딩 출력 */
            with(binding) {
                viewDetail = record
            }
        }

    }

}