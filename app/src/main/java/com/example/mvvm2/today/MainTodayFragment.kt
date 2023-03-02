package com.example.mvvm2.today

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm2.R
import com.example.mvvm2.ItemSetOnClickListenerInterface
import com.example.mvvm2.databinding.FragmentMainTodayBinding
import com.example.mvvm2.databinding.TodayListItemBinding
import com.example.mvvm2.detail.DetailFragment
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.viewmodel.*
import kotlinx.android.synthetic.main.fragment_main_record.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
class MainTodayFragment : Fragment() {

    /** 바인딩*/
    private lateinit var binding: FragmentMainTodayBinding

    /** viewModel */
    private val todayViewModel: TodayViewModel by viewModels(factoryProducer = { viewModelFactory })
    private val mainViewModel: MainViewModel by viewModels({ requireActivity() }, factoryProducer = { viewModelFactory })
    private val detailViewModel: DetailViewModel by viewModels({ requireActivity() }, factoryProducer = { viewModelFactory })

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory

    /** record */
    lateinit var record: RecordEntity

    /** todayRecordList */
    lateinit var todayRecordList: MutableList<RecordEntity>

    /** 어뎁터 */
    lateinit var adapter: MainTodayRecyclerViewAdapter

    /** date, time */
    private val current by lazy {
        LocalDateTime.now()
    }
    private val today by lazy {
        current.format(DateTimeFormatter.ISO_DATE)
    }

    var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTodayFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_today, container, false)
        return binding.root
    }

    /** init */
    private fun initTodayFragment() {
        initViewModelFactory()
        setObserver()
        getTodayRecordList()
    }

    private fun initViewModelFactory() {
        viewModelFactory = ViewModelFactory(RecordRepository())
    }

    private fun setObserver() {
        /** 조회 옵저버 */
        todayViewModel.isGetDateDataComplete.observe(this) {
            todayRecordList = it.toMutableList()
            setRecyclerView()
            adapter.notifyItemChanged(position)
        }
        /** 수정 옵저버 */
        detailViewModel.isUpdateDataComplete.observe(requireActivity()) {
            if(::todayRecordList.isInitialized) {
//                if(mainViewModel.selectRecord.date == today) {
                    todayRecordList[position] = mainViewModel.selectRecord
                    adapter.notifyItemChanged(position)
//                }
            }
        }
        /** 삭제 옵저버 */
        detailViewModel.isDeleteDataComplete.observe(requireActivity()) {
            if(::todayRecordList.isInitialized) {
                val position = todayRecordList.indexOf(it)
                if(position != -1) {
                    todayRecordList.removeAt(position)

                    adapter.notifyItemRemoved(position)
                }
            }
        }
    }

    private fun getTodayRecordList(){
        todayViewModel.getDateData(today)
    }

    /** 리사이클러뷰 출력 */
    private fun setRecyclerView() {
        adapter = MainTodayRecyclerViewAdapter()
        adapter.recordList = todayRecordList
        binding.todayList.adapter = adapter
        binding.todayList.layoutManager = LinearLayoutManager(requireContext())

        /** 리스트 클릭*/
        adapter.listItemClickFunc(object: ItemSetOnClickListenerInterface {
            override fun listItemClickListener(itemData: RecordEntity, binding: TodayListItemBinding) {
                position = todayRecordList.indexOf(itemData)
                mainViewModel.selectRecord = itemData
                parentFragmentManager.beginTransaction()
                    .replace(R.id.detail_frag, DetailFragment())
                    .commit()

                /** detail_frag 출력 */
                mainViewModel.setVisibilityDetailFragment(true)
            }
        })
    }
}