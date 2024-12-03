package com.example.herat_beat.logic

import HomeRecord
import com.example.herat_beat.databases.DatabaseHelper
import com.example.herat_beat.tool.FormatUtils

object Repository {
    private val database = DatabaseHelper()  //获得类的实例
    fun saveData(startTime: String,count: Int, clickCount: Int){
        val ite=HomeRecord(0,startTime, FormatUtils.getDate(),0,"0","0")
        database.insertHome(ite)
        database.printTableData1()
        val mid= database.printTableData()
        database.updateHome(mid,count, clickCount.toString())
    }
    fun update(count:Int, clickCount:Int){
        val id= database.printTableData()
        database.updateHome(id,count,clickCount.toString())
    }
}