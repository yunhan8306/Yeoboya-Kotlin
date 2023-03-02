package com.example.mvvm2.total

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mvvm2.R
import com.example.mvvm2.DetailItemSetOnClickListenerInterface
import com.example.mvvm2.databinding.FragmentDetailBinding
import com.example.mvvm2.detail.ViewPagerAdapter
import com.example.mvvm2.entity.RecordEntity

class MainTotalRecyclerViewAdapter(
): RecyclerView.Adapter<MainTotalRecyclerViewAdapter.MainTotalRecyclerViewHolder>() {

    var recordList = mutableListOf<RecordEntity>()

    /** 어뎁터 */
    lateinit var adapter: ViewPagerAdapter

    // interface 객체 생성
    private var onClickListener: DetailItemSetOnClickListenerInterface? = null

    // Activity에서 호출 시 객체 초기화
    fun removeClickListener(pOnClick: DetailItemSetOnClickListenerInterface) {
        this.onClickListener = pOnClick
    }

    fun updateClickListener(pOnClick: DetailItemSetOnClickListenerInterface) {
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

            /** 뷰페이저 출력 */
            adapter = ViewPagerAdapter()
            var uriList = record.uriList.split("^")

            adapter.uriList = uriList
            binding.viewPager.adapter = adapter
            binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            /** 수정, 삭제 */
            // 클릭하고자 하는 view의 리스너에 데이터 전달
            if(adapterPosition != RecyclerView.NO_POSITION){
                binding.btnRemove.setOnClickListener {
                    onClickListener?.removeClickListener(record, binding)
                }

                binding.btnUpdate.setOnClickListener {
                    onClickListener?.updateClickListener(record, binding)
                }
            }
        }
    }
}

