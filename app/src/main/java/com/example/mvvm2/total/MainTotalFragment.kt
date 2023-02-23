package com.example.mvvm2.total

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.R
import com.example.mvvm2.databinding.FragmentMainTotalBinding
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.viewmodel.DetailViewModel
import com.example.mvvm2.viewmodel.TotalViewModel
import com.example.mvvm2.viewmodel.ViewModelFactory


class MainTotalFragment : Fragment() {

    /** 데이터바인딩*/
    private lateinit var binding: FragmentMainTotalBinding

    /** viewModel */
    lateinit var totalViewModel: TotalViewModel
    lateinit var detailViewModel: DetailViewModel

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory

    /** totalRecordList */
    lateinit var totalRecordList: MutableList<RecordEntity>

    /** 어뎁터 */
    lateinit var adapter: MainTotalRecyclerViewAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainTotalFragment - onCreate called")
        initTotalFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_total, container, false)
        return binding.root
    }



    private fun initTotalFragment() {
        initViewModel()
        setObserver()
        getTotalRecordList()
    }

    private fun initViewModel() {
        viewModelFactory = ViewModelFactory(RecordRepository())
        totalViewModel = ViewModelProvider(this, viewModelFactory)[TotalViewModel::class.java]
    }

    private fun setObserver() {
        totalViewModel.isGetAllComplete.observe(this) {
            totalRecordList = it.toMutableList()
            setRecyclerView()
            Log.d(TAG, "totalRecordList - $totalRecordList")
        }
    }

    private fun getTotalRecordList() {
        totalViewModel.getAll()
    }

    private fun setRecyclerView() {
        adapter = MainTotalRecyclerViewAdapter(requireContext(), totalRecordList, detailViewModel)
        binding.totalList.adapter = adapter
        binding.totalList.layoutManager = LinearLayoutManager(requireContext())
    }

}