package com.example.herat_beat.logic.service

interface ServiceCallback  {
    //接口的作用是:规范一种范式，让实现类去实现这个范式，让调用类直接使用这个范式。
    fun onUpdateText(text: String){

    }

}