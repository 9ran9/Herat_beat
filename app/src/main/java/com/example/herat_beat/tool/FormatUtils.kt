package com.example.herat_beat.tool

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object FormatUtils {
    /**
     * 格式化时间
     * @param time 时间戳，单位：毫秒
     * @return 格式化后的时间字符串
     */
    @SuppressLint("DefaultLocale")
    fun formatTime(time: Long): String {
        val s = time/1000 % 60
        val m = time/60000 % 60
        return String.format("%02d:%02d",m,s) //格式化String
    }
    /**
     * 得到当前时间
     * @return 当前时间
     */

    fun getCurrent():String{
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return  dateFormat.format(calendar.getTime())
    }

    /**
     * 得到当前日期
     * @return 当前日期
     */
    fun getDate():String{
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.getTime())
    }

}