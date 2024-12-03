package com.example.herat_beat.logic.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import com.example.herat_beat.R
import com.example.herat_beat.ui.count.FetalMove

class XuanfuService : Service(){
    private var callback: ServiceCallback? = null
    @SuppressLint("StaticFieldLeak")
    companion object{
        const val TAG = "XuanfuService"
        fun isStarted():Boolean= instance!=null

        private var instance: XuanfuService? = null

    }
    private lateinit var WindowManager :WindowManager
    private lateinit var layoutParams:WindowManager.LayoutParams
    private lateinit var floateButton:View
    private var notificationManager: NotificationManager? = null

    //有点想做一个livedata类了，还需要处理数据的回调
    private var count = 0
    private var lastClickTime = 0L

    @SuppressLint("RtlHardcoded")
    override fun onCreate() {
        super.onCreate()
        instance = this  //获得服务实列,很重要的

        floateButton = LayoutInflater.from(this).inflate(R.layout.xuanfu, null);
        Log.d(TAG, "onCreate: 进入悬浮：$instance")
        WindowManager= getSystemService(WINDOW_SERVICE) as WindowManager
        layoutParams=android.view.WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = android.view.WindowManager.LayoutParams.TYPE_PHONE
        }
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP
        layoutParams.flags =
            android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams.width = WRAP_CONTENT
        layoutParams.height = WRAP_CONTENT
        layoutParams.gravity = Gravity.TOP or Gravity.START
        layoutParams.x = 300
        layoutParams.y = 300
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun showNotification() {
        val manager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(NotificationChannel("999","vip",NotificationManager.IMPORTANCE_DEFAULT))
        }
        val notification=NotificationCompat.Builder(this,"999")
            .setContentTitle("efs")
            .setContentText("adad")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()
        manager.notify(1,notification)

}

    private val mBinder = CountBinder()
    inner class CountBinder : Binder() {
        //处理数据绑定的逻辑
        fun initData(count: Int,lastClickTime:Long) {
            //用instance来处理完成数据的使用
            instance?.count = count
            instance?.lastClickTime = lastClickTime
        }
        fun getService(): XuanfuService {
            return this@XuanfuService
        }
    }

    override  fun onBind(intent: Intent?): IBinder{
        return mBinder
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //每次调用时的初始化配置
        showFloatingWindow()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null //这写法，真的强啊
    }


    private fun showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            Log.d(TAG, "showFloatingWindow: ")
            floateButton.findViewById<ImageView>(R.id.baba).setAlpha(0.68f)

            if(floateButton.parent!=null){
                //避免多次点击照成重复创建
                WindowManager.removeView(floateButton)
            }
            WindowManager.addView(floateButton,layoutParams)

            floateButton.setOnTouchListener(FloatingOnTouchListener())
            floateButton.setOnClickListener {
                val scalegetAnim = getScaleAnimation()
                it.startAnimation(scalegetAnim)
                it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
               val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - lastClickTime
                lastClickTime = currentTime
                if (elapsedTime > FetalMove.FIVE_MINUTES_IN_MILLIS) {
                    // 超过5分钟，计数器加一
                    showNotification()
                    count++
                    val intent = Intent("com.example.MY_CUSTOM_ACTION")
                    intent.putExtra("message", "$count")
                    sendBroadcast(intent) //发送广播，完成数据的传递，感觉可以换一个实现方式。
                }
                else{
                    showNotification()
                    performEvent()
                    val intent = Intent("com.example.MY_CUSTOM_ACTION")
                    intent.putExtra("message", "yes")
                    sendBroadcast(intent)
                }

            }
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
    fun getScaleAnimation(): Animation? {
        //实例化 ScaleAnimation 主要是缩放效果
        //参数：fromX-动画开始前，x坐标   toX-动画结束后x坐标
        //fromY-动画开始前，Y坐标  toY-动画结束后Y坐标
        //pivotXType - 为动画相对于物件的X坐标的参照物   pivotXValue - 值
        //pivotYType - 为动画相对于物件的Y坐标的参照物   pivotYValue - 值
        val animation: Animation = ScaleAnimation(
            1.0f, 0.9f, 1.0f, 0.9f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        //设置动画插值器 被用来修饰动画效果,定义动画的变化率
        animation.interpolator = DecelerateInterpolator()
        //设置动画执行时间
        animation.duration = 200
        return animation
    }
    private fun performEvent() {
        val eventData = "Event data"
        triggerCallback(eventData)
    }

    private fun triggerCallback(data: String) {
        callback?.onUpdateText(data)
    }


    private inner class FloatingOnTouchListener : View.OnTouchListener{
        private  var x:Int=0
        private var y:Int=0
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            Log.d("点击","进入点击事件")
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event!!.rawX.toInt()
                    y = event!!.rawY.toInt()
                }

                MotionEvent.ACTION_MOVE -> {
                    val nowX = event!!.rawX.toInt()
                    val nowY = event!!.rawY.toInt()
                    val movedX = nowX - x
                    val movedY = nowY - y
                    x = nowX
                    y = nowY
                    layoutParams.x = layoutParams.x + movedX
                    layoutParams.y = layoutParams.y + movedY
                    WindowManager.updateViewLayout(v, layoutParams)
                }

                else -> {}
            }

            return false
        }
    }
}
