package com.example.herat_beat.ui.count

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.herat_beat.ui.count.FetalMove.Companion.FIVE_MINUTES_IN_MILLIS
import com.example.herat_beat.tool.TimeUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import com.example.herat_beat.logic.Repository
import com.example.herat_beat.tool.FormatUtils.formatTime
import com.example.herat_beat.tool.FormatUtils.getCurrent
import kotlinx.coroutines.Dispatchers

class FetalMoveModel : ViewModel(){

    companion object{
        const val TAG = "TaiDongModel"
    }

    /**
     *基本数据定义以及一些方法
     */

    //倒计时
    data class RemainTime(val percentage: Float,val remainTime:String)
    private val countdown  = MutableLiveData<RemainTime>()
    val countdownModel: LiveData<RemainTime> get()=countdown
    private fun changeCountdown(percentage: Float=0f,string: String="00:00"){
        countdown.value = countdown.value?.
        copy(percentage = percentage, remainTime = string)?:
                RemainTime(percentage,string)
    }
    //控制协程的变量：
    private var job: Job?= null
    //启动倒计时
    private fun startCountdown(duration:Long){
        //开一个viewModel的协程域，它是后台IO线程
        job = viewModelScope.launch {
            var time= duration
            //循环实现倒计时
            while (isActive && time>0){
                //这个isActive是协程是判断协程是否活跃的意思
                //其实协程一直都在就看我们用不用它了。
                //所以也是为了能用job.cancel立刻关掉它
                time -=1000
                changeCountdown((duration-time)*100f/duration, formatTime(time) )
                delay(1000)
            }

        }
    }
    //停止倒计时：
    private fun stopCountdown(){
        job?.cancel()
        changeCountdown()
    }

    //判断开始与否：
    data class Start(val isStart:Boolean=false,val startTime:String="00:00")
    private val  start = MutableLiveData(Start())
    val startModel: LiveData<Start> get() = start
    private fun changeIsStart(isStart: Boolean=false,startTime: String="00:00"){
        start.value = start.value?.copy(isStart = isStart, startTime = startTime)?:
                Start(isStart,startTime)
    }
    fun getIsStart() = start.value?.isStart?:false
    private fun getStartTime() = start.value?.startTime?: "00:00"

    //点击次数:
    private val click = MutableLiveData(0)
    val clickModel : LiveData<Int> get() = click
    private fun getClickCount() = click.value?:0
    private fun addClickCount(){
        click.value = getClickCount()+1
    }
    //有效点击次数：
    private val count = MutableLiveData(0)
    val countModel: LiveData<Int> get() = count
    fun getCount() = count.value?:0
    private fun addCount(){
        count.value = getCount()+1
    }
    private fun changCount(count: Int=0){
        this.count.value = count
    } //重载
    fun changeCount(count:String="0")=changCount(count.toInt())

    //最后点击时间
    private val lastClickTime = MutableLiveData(0L)//不着急，外界不一定要他
    fun getLastClickTime() = lastClickTime.value?:0L



    /**
     * 对外的两个可以改变值的方法：
     * click()：点击方法，修改数值，倒计时，点击次数，最后点击时间，数据库存数据
     * clearAll()：清空所有值，包括倒计时，点击次数，最后点击时间，数据库
     */
    fun click(){
        if (!getIsStart()){
            //修改数值
            changeIsStart(true, getCurrent())
            startCountdown(TimeUtils.HOUR) //一小时倒计时
            addCount()
            addClickCount()
            lastClickTime.value = System.currentTimeMillis()
            //数据库存数据
            viewModelScope.launch(Dispatchers.IO) {
                //ID线程处理数据
                Repository.saveData(getStartTime(),getCount(),getClickCount())
            }
        }
        else{
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - getLastClickTime()
            lastClickTime.value=currentTime
            // 超过5分钟，计数器加一
            if (elapsedTime > FIVE_MINUTES_IN_MILLIS) {
                addCount()
            }
            addClickCount()
            //存当前数值
            Repository.update(getCount(),getClickCount())
        }
    }

    fun clearAll(){
        changeCountdown()
        changeIsStart()
        stopCountdown()
        click.value=0
        changCount()
        lastClickTime.value=0L
    }

}