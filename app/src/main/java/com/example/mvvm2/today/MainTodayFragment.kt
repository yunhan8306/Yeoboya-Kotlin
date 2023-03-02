package com.example.mvvm2.today

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm2.R
import com.example.mvvm2.ItemSetOnClickListenerInterface
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.databinding.FragmentMainTodayBinding
import com.example.mvvm2.databinding.TodayListItemBinding
import com.example.mvvm2.detail.DetailFragment
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.viewmodel.*
import kotlinx.android.synthetic.main.fragment_main_record.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainTodayFragment : Fragment() {

    /** 바인딩*/
    private lateinit var binding: FragmentMainTodayBinding

    /** viewModel */
    private val todayViewModel: TodayViewModel by viewModels({ requireActivity() }, factoryProducer = { viewModelFactory })
    private val mainViewModel: MainViewModel by viewModels({ requireActivity() })
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
    lateinit var today: String

    var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainTodayFragment - onCreate called")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_today, container, false)
        initTodayFragment()
        return binding.root
    }

    /** init */
    @RequiresApi(Build.VERSION_CODES.O)
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
        todayViewModel.isGetDateDataComplete.observe(requireActivity()) {
            todayRecordList = it.toMutableList()
            setRecyclerView()
//            adapter.notifyItemChanged(position)
        }
        /** 수정 옵저버 */
        detailViewModel.isUpdateDataComplete.observe(requireActivity()) {
            if(::todayRecordList.isInitialized) {
//                todayRecordList.takeIf { it.isNotEmpty() }.let {
//                    it?.set(position, mainViewModel.selectRecord)
//                    adapter.notifyItemChanged(position)
//                }
                if(todayRecordList.indexOf(it) != -1) {
                    adapter.notifyItemChanged(position)
                }
            }
        }
        /** 삭제 옵저버 */
        detailViewModel.isDeleteDataComplete.observe(requireActivity()) {
            if(::todayRecordList.isInitialized) {
                if(todayRecordList.indexOf(it) != -1) {
                    todayRecordList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTodayRecordList(){
        today = let {
            val current = LocalDateTime.now()
            current.format(DateTimeFormatter.ISO_DATE)
        }

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