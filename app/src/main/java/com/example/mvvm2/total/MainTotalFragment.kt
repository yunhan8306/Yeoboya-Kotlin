package com.example.mvvm2.total

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.R
import com.example.mvvm2.DetailItemSetOnClickListenerInterface
import com.example.mvvm2.databinding.FragmentDetailBinding
import com.example.mvvm2.databinding.FragmentMainTotalBinding
import com.example.mvvm2.detail.DetailFragment
import com.example.mvvm2.detail.DetailUpdateFragment
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.viewmodel.DetailViewModel
import com.example.mvvm2.viewmodel.MainViewModel
import com.example.mvvm2.viewmodel.TotalViewModel
import com.example.mvvm2.viewmodel.ViewModelFactory


class MainTotalFragment : Fragment() {

    /** 바인딩 */
    private lateinit var binding: FragmentMainTotalBinding

    /** viewModel */
    private val totalViewModel: TotalViewModel by viewModels(factoryProducer = { viewModelFactory })
    private val mainViewModel: MainViewModel by viewModels({ requireActivity() })
    private val detailViewModel: DetailViewModel by viewModels({requireActivity()},{ viewModelFactory })

    /** viewModelFactory */
    private lateinit var viewModelFactory: ViewModelFactory

    /** totalRecordList */
    lateinit var totalRecordList: MutableList<RecordEntity>

    /** 어뎁터 */
    lateinit var mainTotalRecyclerViewAdapter: MainTotalRecyclerViewAdapter

    var position = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initTotalFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_total, container, false)
        return binding.root
    }

    /** init */
    private fun initTotalFragment() {
        initViewModelFactory()
        setObserver()
        getTotalRecordList()
    }

    private fun initViewModelFactory() {
        viewModelFactory = ViewModelFactory(RecordRepository())
    }

    private fun setObserver() {
        totalViewModel.isGetAllComplete.observe(this) {
            totalRecordList = it.toMutableList()
            setRecyclerView()
        }
        detailViewModel.isDeleteDataComplete.observe(requireActivity()) {
            if(::totalRecordList.isInitialized) {
                val position = totalRecordList.indexOf(it)

                if(position != -1) {
                    totalRecordList.removeAt(position)
                    mainTotalRecyclerViewAdapter.notifyItemRemoved(position)
                }
            }
        }
        detailViewModel.isUpdateDataComplete.observe(requireActivity()) {
            if(::totalRecordList.isInitialized) {
                val position = totalRecordList.indexOf(it)

                if(position != -1) {
                    totalRecordList[position] = mainViewModel.selectRecord
                    mainTotalRecyclerViewAdapter.notifyItemChanged(position)
                }
            }
        }
    }

    private fun getTotalRecordList() {
        totalViewModel.getAll()
    }

    private fun setRecyclerView() {
        mainTotalRecyclerViewAdapter = MainTotalRecyclerViewAdapter()
        mainTotalRecyclerViewAdapter.recordList = totalRecordList
        binding.totalList.apply {
            adapter = mainTotalRecyclerViewAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }

        mainTotalRecyclerViewAdapter.removeClickListener(object: DetailItemSetOnClickListenerInterface {
            override fun updateClickListener(itemData: RecordEntity, binding: FragmentDetailBinding) {
                position = totalRecordList.indexOf(itemData)
                mainViewModel.selectRecord = itemData
                parentFragmentManager.beginTransaction()
                    .replace(R.id.detail_frag, DetailUpdateFragment())
                    .commit()

                /** detail_frag 출력 */
                mainViewModel.setVisibilityDetailFragment(true)
            }

            override fun removeClickListener(itemData: RecordEntity, binding: FragmentDetailBinding) {
                detailViewModel.deleteData(itemData)
            }
        })
    }
}