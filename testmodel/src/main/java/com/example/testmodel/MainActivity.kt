package com.example.testmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.herat_beat.Utils.TDtimer
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tc=findViewById<TextView>(R.id.tvv)
        val td= TDtimer(textView = tc)
        td.startTimer()
        val date= Date(td.starttime)
        val dateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())
        println(dateFormat.format(date))
        findViewById<TextView>(R.id.textView2).text=dateFormat.format(date)
    }
}