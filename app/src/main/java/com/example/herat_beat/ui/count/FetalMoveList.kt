package com.example.herat_beat.ui.count

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.herat_beat.view.linechart.BaseLinerView
import com.example.herat_beat.R
import com.example.herat_beat.databases.DatabaseHelper
import com.example.herat_beat.tool.FormatUtils.getDate

class FetalMoveList : Fragment() {
   lateinit var   baseLinerView: BaseLinerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_taidong_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("tubiao")
        val list= DatabaseHelper().getAllHomebydate(getDate())
        initLine()
        baseLinerView.mView = view.findViewById(R.id.liner)
        baseLinerView.lineColor = "#7EC4F6"
        baseLinerView.lineColor2 = "#7EC4F6"
        baseLinerView.zeroLine = 0
        baseLinerView.minValue = 0
        baseLinerView.maxValue = 100
        baseLinerView.initView()
        for (i in list){
            if (i.frequency.toInt()!=0){
                baseLinerView.onUdata(i.frequency.toFloat()*6)
            }
        }
    }
    private fun initLine(){
        baseLinerView=object : BaseLinerView(){
            override fun getLineName(): String {
                return "每日胎动推算次数"
            }
        }
    }
}
