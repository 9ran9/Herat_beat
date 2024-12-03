package com.example.herat_beat.ui.record

import TimeRecord
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.herat_beat.R


class TimeRecordAdapter(private val dataList: ArrayList<TimeRecord>) : RecyclerView.Adapter<TimeRecordAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val dataid =dataList[position].id
                Log.d("item","$position")
                Log.d("item","$dataid")

                listener?.onItemClick(dataid)

            }}
        // 在 ViewHolder 中获取子视图的引用
             val time1TextView: TextView = itemView.findViewById(R.id.textView1)
             val time2TextView: TextView = itemView.findViewById(R.id.textView2)
             val time3TextView: TextView = itemView.findViewById(R.id.textView3)
             val time4TextView: TextView = itemView.findViewById(R.id.textView4)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 创建 ViewHolder 对象并返回
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 获取当前行的时间记录列表
        val timeRecords = dataList[position]
        // 在此方法中将数据绑定到 ViewHolder 的子视图上
             holder.time1TextView.text = timeRecords.startTime
             holder.time2TextView.text = timeRecords.Howlong
             holder.time3TextView.text = timeRecords.Jiange
             holder.time4TextView.text = timeRecords.hurt
        val roundedBackground = ContextCompat.getDrawable(holder.itemView.context, R.drawable.whiteround)
        val roundedBackground2 = ContextCompat.getDrawable(holder.itemView.context, R.drawable.round_coner)
        if (position % 2 == 1) {
            holder.itemView.background=roundedBackground
        } else {
            holder.itemView.background=roundedBackground2
        }
    }

    override fun getItemCount(): Int {
        // 返回数据集的大小（行数）
        return dataList.size
    }

    interface OnItemClickListener {
        fun onItemClick(itemId: Int)
    }


}