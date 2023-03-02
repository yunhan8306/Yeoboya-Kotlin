package com.example.mvvm2.grid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mvvm2.GridItemSetOnClickListenerInterface
import com.example.mvvm2.R
import com.example.mvvm2.databinding.FragmentMainGridBinding
import com.example.mvvm2.databinding.GridImageViewBinding
import com.example.mvvm2.databinding.TodayListItemBinding
import com.example.mvvm2.detail.DetailFragment
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.total.MainTotalRecyclerViewAdapter
import com.example.mvvm2.viewmodel.*


class MainGridFragment : Fragment() {

    /** 바인딩*/
    private lateinit var binding: FragmentMainGridBinding

    /** viewModel */
    private val totalViewModel: TotalViewModel by viewModels(factoryProducer = { viewModelFactory })
    private val mainViewModel: MainViewModel by viewModels({ requireActivity() }, factoryProducer = { viewModelFactory })
    private val detailViewModel: DetailViewModel by viewModels({requireActivity()}, factoryProducer = { viewModelFactory })

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory

    /** record */
    lateinit var record: RecordEntity

    /** todayRecordList */
    lateinit var gridRecordList: MutableList<RecordEntity>

    /** 어뎁터 */
    lateinit var mainGridRecyclerViewAdapter: MainGridRecyclerViewAdapter

    var position = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTodayFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_grid, container, false)
        return binding.root
    }

    private fun initTodayFragment() {
        initViewModel()
        setObserver()
        getTotalRecordList()
    }

    private fun initViewModel() {
        viewModelFactory = ViewModelFactory(RecordRepository())
    }

    private fun getTotalRecordList() {
        totalViewModel.getAll()
    }

    private fun setRecyclerView() {
        mainGridRecyclerViewAdapter = MainGridRecyclerViewAdapter()
        mainGridRecyclerViewAdapter.recordList = gridRecordList
        binding.gridList.apply {
            adapter = mainGridRecyclerViewAdapter
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        }

        mainGridRecyclerViewAdapter.listItemClickFunc(object: GridItemSetOnClickListenerInterface {
            override fun listItemClickListener(itemData: RecordEntity, binding: GridImageViewBinding) {
                position = gridRecordList.indexOf(itemData)
                mainViewModel.selectRecord = itemData
                parentFragmentManager.beginTransaction()
                    .replace(R.id.detail_frag, DetailFragment())
                    .commit()

                /** detail_frag 출력 */
                mainViewModel.setVisibilityDetailFragment(true)
            }
        })
    }

    private fun setObserver() {
        totalViewModel.isGetAllComplete.observe(this) {
            gridRecordList = it.toMutableList()
            setRecyclerView()
        }
        detailViewModel.isDeleteDataComplete.observe(requireActivity()) {
            if(::gridRecordList.isInitialized) {
                val position = gridRecordList.indexOf(it)

                if(position != -1) {
                    gridRecordList.removeAt(position)

                    mainGridRecyclerViewAdapter.notifyItemRemoved(position)
                }
            }
        }
    }
}