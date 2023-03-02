package com.example.mvvm2

import com.example.mvvm2.databinding.FragmentDetailBinding
import com.example.mvvm2.databinding.GridImageViewBinding
import com.example.mvvm2.databinding.TodayListItemBinding
import com.example.mvvm2.entity.RecordEntity

interface ItemSetOnClickListenerInterface {
    fun listItemClickListener(itemData: RecordEntity, binding: TodayListItemBinding)
}

interface DetailItemSetOnClickListenerInterface {
    fun removeClickListener(itemData: RecordEntity, binding: FragmentDetailBinding)

    fun updateClickListener(itemData: RecordEntity, binding: FragmentDetailBinding)
}

interface GridItemSetOnClickListenerInterface {
    fun listItemClickListener(itemData: RecordEntity, binding: GridImageViewBinding)
}