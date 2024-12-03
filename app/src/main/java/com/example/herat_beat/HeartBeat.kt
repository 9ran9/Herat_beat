package com.example.herat_beat

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context


class HeartBeat :Application(){
    @SuppressLint("StaticFieldLeak")
    companion object{
        lateinit var context:Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}