package com.example.mvvm2.total

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm2.R
import com.example.mvvm2.SetOnClickListenerInterface
import com.example.mvvm2.databinding.FragmentDetailBinding
import com.example.mvvm2.entity.RecordEntity

class MainTotalRecyclerViewAdapter(
): RecyclerView.Adapter<MainTotalRecyclerViewAdapter.MainTotalRecyclerViewHolder>() {

    var recordList = mutableListOf<RecordEntity>()

    // interface 객체 생성
    private var onClickListener: SetOnClickListenerInterface? = null

    // Activity에서 호출 시 객체 초기화
    fun listItemClickFunc(pOnClick: SetOnClickListenerInterface) {
        this.onClickListener = pOnClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainTotalRecyclerViewHolder {
        return MainTotalRecyclerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_detail, parent, false))
    }

    override fun onBindViewHolder(holder: MainTotalRecyclerViewHolder, position: Int) {
        val record = recordList[position]
        holder.bind(record)
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    inner class MainTotalRecyclerViewHolder(val binding: FragmentDetailBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(record: RecordEntity) {
            /** 데이터 바인딩 출력 */
            with(binding) {
                viewDetail = record
            }

            /** 이미지 뷰페이저 필요 */
        }
    }
}

