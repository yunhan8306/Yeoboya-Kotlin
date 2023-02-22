package com.example.mvvm2.today

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.mvvm2.R
import com.example.mvvm2.databinding.FragmentMainRecordBinding
import com.example.mvvm2.databinding.FragmentMainTodayBinding


class MainTodayFragment : Fragment() {

    /** 데이터바인딩*/
    private lateinit var binding: FragmentMainTodayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_today, container, false)

        return binding.root
    }


}