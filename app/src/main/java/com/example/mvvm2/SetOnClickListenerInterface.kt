package com.example.mvvm2

import com.example.mvvm2.databinding.FragmentDetailBinding
import com.example.mvvm2.databinding.TodayListItemBinding
import com.example.mvvm2.entity.RecordEntity

interface SetOnClickListenerInterface {
    fun listItemClickListener(itemData: RecordEntity, binding: TodayListItemBinding)
}

interface SetOnClickListenerInterface2 {
    fun removeClickListener(itemData: RecordEntity, binding: FragmentDetailBinding)

    fun updateClickListener(itemData: RecordEntity, binding: FragmentDetailBinding)
}