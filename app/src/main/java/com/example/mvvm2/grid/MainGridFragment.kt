package com.example.mvvm2.grid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm2.R
import com.example.mvvm2.databinding.FragmentMainGridBinding
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.viewmodel.*


class MainGridFragment : Fragment() {

    /** 바인딩*/
    private lateinit var binding: FragmentMainGridBinding

    /** viewModel */
    lateinit var totalViewModel: TotalViewModel
    lateinit var mainViewModel: MainViewModel

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory

    /** record */
    lateinit var record: RecordEntity

    /** todayRecordList */
    lateinit var gridRecordList: MutableList<RecordEntity>

    /** 어뎁터 */
    lateinit var adapter: MainGridRecyclerViewAdapter

    var position = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTodayFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_grid, container, false)
    }

    fun initTodayFragment() {
        initViewModel()
    }

    private fun initViewModel() {
        viewModelFactory = ViewModelFactory(RecordRepository())
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]
        totalViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[TotalViewModel::class.java]
    }


}