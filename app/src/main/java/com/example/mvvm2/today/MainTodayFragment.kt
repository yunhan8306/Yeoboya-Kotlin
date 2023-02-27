package com.example.mvvm2.today

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.R
import com.example.mvvm2.SetOnClickListenerInterface
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


    /** 데이터바인딩*/
    private lateinit var binding: FragmentMainTodayBinding

    /** viewModel */
    lateinit var todayViewModel: TodayViewModel

    /** viewModelFactory */
    lateinit var viewModelFactory: ViewModelFactory

    /** record */
    lateinit var record: RecordEntity

    /** todayRecordList */
    lateinit var todayRecordList: MutableList<RecordEntity>

    /** 어뎁터 */
    lateinit var adapter: MainTodayRecyclerViewAdapter

    /** activityResultLauncher */
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    var position = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainTodayFragment - onCreate called")
        initTodayFragment()

//        updateRecord()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_today, container, false)

//        binding.btnClose.setOnClickListener {
//            binding.btnClose.visibility = View.GONE
//            binding.detailFrame.visibility = View.GONE
//        }

        return binding.root
    }



    /** init */
    @RequiresApi(Build.VERSION_CODES.O)
    fun initTodayFragment() {
        initViewModel()
        setObserver()
        getTodayRecordList()
    }

    private fun initViewModel() {
        viewModelFactory = ViewModelFactory(RecordRepository())
        todayViewModel = ViewModelProvider(this, viewModelFactory)[TodayViewModel::class.java]
    }

    private fun setObserver() {
        todayViewModel.isGetDateDataComplete.observe(this) {
            todayRecordList = it.toMutableList()
            setRecyclerView()
            Log.d(TAG, "setRecyclerView - $todayRecordList")

            adapter.notifyItemChanged(position)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodayRecordList(){
        /** date, time */
        val current = LocalDateTime.now()
        val today = current.format(DateTimeFormatter.ISO_DATE)

        todayViewModel.getDateData(today)
    }

    private fun setRecyclerView() {

        adapter = MainTodayRecyclerViewAdapter()
        adapter.recordList = todayRecordList
        binding.todayList.adapter = adapter
        binding.todayList.layoutManager = LinearLayoutManager(requireContext())


        adapter.listItemClickFunc(object: SetOnClickListenerInterface {
            override fun listItemClickListener(itemData: RecordEntity, binding: TodayListItemBinding) {

                position = todayRecordList.indexOf(itemData)

                /** detail 프래그먼트로 이동 */
                setFragmentResult("no", bundleOf("bundleKey" to itemData.no.toString()))
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frag, DetailFragment())
                    .commit()
            }
        })
    }

    private fun updateRecord() {
        /** 인텐트 data 호출  @@ getParcelableExtra 수정 필요 */
        Log.d(TAG, "ac 확인")
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.d(TAG, "ac 확인2")
            if(it.resultCode == AppCompatActivity.RESULT_OK) {
                Log.d(TAG, "ac 확인3")
                val intent: Intent? = it.data
                val newRecord: RecordEntity = intent?.getParcelableExtra("updateRecord")!!

                Log.d(TAG, "newRecord - $newRecord")

                todayRecordList[position] = newRecord

                adapter.notifyItemChanged(position)


            }
        }
    }
}