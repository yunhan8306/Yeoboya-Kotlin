package com.example.mvvm2.today
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mvvm2.R
import com.example.mvvm2.ItemSetOnClickListenerInterface
import com.example.mvvm2.databinding.TodayListItemBinding
import com.example.mvvm2.entity.RecordEntity

class MainTodayRecyclerViewAdapter: RecyclerView.Adapter<MainTodayRecyclerViewAdapter.MainTodayRecyclerViewHolder>() {

    var recordList = mutableListOf<RecordEntity>()

    // interface 객체 생성
    private var onClickListener: ItemSetOnClickListenerInterface? = null

    // Activity에서 호출 시 객체 초기화
    fun listItemClickFunc(pOnClick: ItemSetOnClickListenerInterface) {
        this.onClickListener = pOnClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainTodayRecyclerViewHolder {
        return MainTodayRecyclerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.today_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: MainTodayRecyclerViewHolder, position: Int) {
        val record = recordList[position]
        holder.bind(record)
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    inner class MainTodayRecyclerViewHolder(val binding: TodayListItemBinding): ViewHolder(binding.root) {

        fun bind(record: RecordEntity) {
            /** content 줄바꿈 or 한 줄의 내용이 길 때 일부만 출력되게 처리 */
            var content = record.content
            if (content.indexOf("\n") != -1) {
                content = content.substring(0 until content.indexOf("\n")) + " ..."
            } else if(content.length > 12){
                content = content.substring(0, 12) + " ..."
            }
            record.content = content

            /** 데이터 바인딩 출력 */
            with(binding) {
                todayItem = record
            }

            // 클릭하고자 하는 view의 리스너에 데이터 전달
            if(adapterPosition != RecyclerView.NO_POSITION){
                binding.item.setOnClickListener {
                    onClickListener?.listItemClickListener(record, binding)
                }
            }

            /** 이미지 여러개 받는거 수정 필요 */


        }
    }
}


