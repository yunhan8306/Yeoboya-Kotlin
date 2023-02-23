package com.example.mvvm2

import com.example.mvvm2.databinding.TodayListItemBinding
import com.example.mvvm2.entity.RecordEntity

interface SetOnClickListenerInterface {
    fun listItemClickListener(itemData: RecordEntity, binding: TodayListItemBinding)
}