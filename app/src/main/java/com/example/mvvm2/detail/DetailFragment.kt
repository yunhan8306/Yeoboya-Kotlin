package com.example.mvvm2.detail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.R
import com.example.mvvm2.databinding.FragmentDetailBinding
import com.example.mvvm2.entity.RecordEntity
import com.example.mvvm2.grid.MainGridFragment
import com.example.mvvm2.room.RecordRepository
import com.example.mvvm2.today.MainTodayFragment
import com.example.mvvm2.viewmodel.DetailViewModel
import com.example.mvvm2.viewmodel.MainViewModel
import com.example.mvvm2.viewmodel.TodayViewModel
import com.example.mvvm2.viewmodel.ViewModelFactory


class DetailFragment : Fragment() {

    /** viewModel */
    private val detailViewModel: DetailViewModel by viewModels(
        {requireActivity()}, factoryProducer = { viewModelFactory })
    private val mainViewModel: MainViewModel by viewModels(
        {requireActivity() }, factoryProducer = { viewModelFactory })

    /** viewModelFactory */
    private lateinit var viewModelFactory: ViewModelFactory

    /** 바인딩*/
    private lateinit var binding: FragmentDetailBinding

    /** 어뎁터 */
    lateinit var adapter:ViewPagerAdapter

    /** record */
    lateinit var record: RecordEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "DetailFragment - onCreate called")

        initViewModelFactory()

        record = mainViewModel.selectRecord
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail,
            container, false)

        /** 삭제 버튼 클릭 */
        binding.btnRemove.setOnClickListener {
            detailViewModel.deleteData(record)

            /** detail_frag 꺼짐 */
            mainViewModel.setVisibilityDetailFragment(false)
        }

        /** 수정 버튼 클릭 */
        binding.btnUpdate.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.detail_frag, DetailUpdateFragment())
                .commit()
        }

        /** 데이터 바인딩 출력 */
        with(binding) {
            viewDetail = record
        }

        /** 이미지 뷰페이저 출력 */
        adapter = ViewPagerAdapter()
        val uriList = record.uriList.split("^")

        adapter.uriList = uriList
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    private fun initViewModelFactory() {
        viewModelFactory = ViewModelFactory(RecordRepository())
    }
}