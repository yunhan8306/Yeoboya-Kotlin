package com.example.mvvm2.grid

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm2.GridItemSetOnClickListenerInterface
import com.example.mvvm2.MainActivity.Companion.TAG
import com.example.mvvm2.databinding.GridImageViewBinding
import com.example.mvvm2.entity.RecordEntity

class MainGridRecyclerViewAdapter : RecyclerView.Adapter<MainGridRecyclerViewAdapter.MainGridRecyclerViewHolder>() {

    var recordList = listOf<RecordEntity>()

    // interface 객체 생성
    private var onClickListener: GridItemSetOnClickListenerInterface? = null

    // Activity에서 호출 시 객체 초기화
    fun listItemClickFunc(pOnClick: GridItemSetOnClickListenerInterface) {
        this.onClickListener = pOnClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainGridRecyclerViewHolder {
        return MainGridRecyclerViewHolder(
            GridImageViewBinding.inflate(LayoutInflater.from(parent.context),
                parent, false))
    }

    override fun onBindViewHolder(holder: MainGridRecyclerViewHolder, position: Int) {
        val record = recordList[position]
        holder.bind(record)
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    inner class MainGridRecyclerViewHolder(private val binding: GridImageViewBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: RecordEntity){
            with(binding) {
                gridItem = record
                Log.d(TAG, "record")
            }

            if(adapterPosition != RecyclerView.NO_POSITION){
                binding.gridImage.setOnClickListener {
                    onClickListener?.listItemClickListener(record, binding)
                }
            }
        }
    }
}

