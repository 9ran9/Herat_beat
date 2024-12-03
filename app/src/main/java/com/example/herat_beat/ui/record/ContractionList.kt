package com.example.herat_beat.ui.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.herat_beat.databases.DatabaseHelper
import com.example.herat_beat.view.linechart.Line2
import com.example.herat_beat.R

class ContractionList : Fragment() {
    lateinit var   baseLinerView: Line2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_gongsuo_list, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list= DatabaseHelper().getAllItems()


        initLine()
        baseLinerView.mView = view.findViewById(R.id.line3)
        baseLinerView.lineColor = "#F5A2B6"
        baseLinerView.lineColor2 = "#F5A2B6"
        baseLinerView.zeroLine = 0
        baseLinerView.minValue = 0
        baseLinerView.maxValue = 100
        baseLinerView.initView()
        for (item in list)
        {
            val int=item.Jiange
            val minutes = convertTimeStringToMinutes(int)
            println(minutes.toString()+"++++")
            baseLinerView.onUdata(minutes.toFloat())
        }
    }
    fun convertTimeStringToMinutes(timeString: String): Int {
      val char=timeString[0]
        val char2=timeString[1]
        if (char.toInt()==0) return char2.toInt()
        else return timeString.take(2).toInt()
    }
    fun initLine(){
        baseLinerView=object : Line2(){
            override fun getLineName(): String {
                return "平均宫缩间隔时间"
            }
        }
    }

}