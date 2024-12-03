package com.example.herat_beat.ui.count

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.herat_beat.R
import com.example.herat_beat.view.dialog.TestDialog
import com.example.herat_beat.logic.Repository
import com.example.herat_beat.logic.service.ServiceCallback
import com.example.herat_beat.logic.service.XuanfuService
import com.example.herat_beat.tool.AnimationUtils.getScaleAnimation
import com.example.herat_beat.tool.FormatUtils.getCurrent
import com.example.herat_beat.tool.TimeUtils
import com.example.herat_beat.tool.ToastUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.herat_beat.view.CirclePercentView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//FM:fetal movements
class FetalMove : Fragment(), ServiceCallback {
    //字体大小改变
    private lateinit var scaleAnimation: Animation
    private lateinit var circlePercentView: CirclePercentView

    private lateinit var countdownTime: TextView   //倒计时时间
    private lateinit var startTime: TextView  //开始时间
    private lateinit var clickCount: TextView  //点击次数
    private lateinit var btnStar: ImageView   //点击按钮 新

    private lateinit var btnStop: ImageView //停止按钮
    private lateinit var btnService: ImageView //悬浮窗
    private lateinit var btnitem: ImageView

    private val PREFS_NAME = "MyPrefsFile"
    private val FIRST_TIME_KEY = "isFirstTime"

    private var isBinder = false
    private var xuanfuService: XuanfuService? = null //服务
    lateinit var countBinder: XuanfuService.CountBinder//获得连接的接口
    //服务
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            countBinder = service as XuanfuService.CountBinder
            //获得binder，以回调的方式完成两者的通信
            countBinder.initData(viewModel.getCount(),viewModel.getLastClickTime())//传递数据
            isBinder = true //表示已经绑定了服务
            xuanfuService = countBinder.getService()
            //得到服务实列，在绑定状态下可以调用服务的方法，从而干涉服务的运行
            Log.d(TAG, "onServiceConnected: 服务连接成功")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected: 服务连接断开")
        }
    }

    companion object {
        const val FIVE_MINUTES_IN_MILLIS = 5 * 60 * 1000
        const val TAG = "FetalMove"
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[FetalMoveModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /**
         * 初始化视图
         */
       val view= inflater.inflate(R.layout.fragment_new_taidong, container, false)
        startTime=view.findViewById(R.id.starttime)    //开始时间
        clickCount=view.findViewById(R.id.pattime)      //点击次数
        countdownTime=view.findViewById(R.id.lasttime)       //倒计时时间
        btnStar=view.findViewById(R.id.circleImageView)  //点击按钮
        btnStop=view.findViewById(R.id.pause)          //结束按钮
        btnService=view.findViewById(R.id.servicee)     //开启悬浮窗
        btnitem=view.findViewById(R.id.konwige)         //小知识按钮
        circlePercentView = view.findViewById(R.id.circlePercentView)
        scaleAnimation = AnimationUtils.loadAnimation(requireContext(),R.anim.scale) //字体变化动画

        /**
         * 用偏好存储来处理首次运行的情况，确保只执行一次。
         */
        val preferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isFirstTime = preferences.getBoolean(FIRST_TIME_KEY, true)
        if (isFirstTime) {
            Repository.saveData("0",0,0)//初始数据
            val editor = preferences.edit()
            editor.putBoolean(FIRST_TIME_KEY, false)
            editor.apply()
        }
        return view
    }

    /**
     * 用ActivityResult API申请悬浮窗权限,
     * 由于需要用户手动打开，所以采用StartActivityForResult进行跳转
     * 其实本来可以在这里写逻辑的，但是不知道为什么尽管赋予了权限还是显示无权限
     * 所以就起到一个界面跳转的作用
     */
    private val startServiceLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
    }

    /**
     * 服务回调：用于同步数据
     */
    override fun onUpdateText(text: String) {
        viewModel.changeCount(text)
        Log.d(TAG, "onUpdateText: 更新数据$text")
    }

    /**
     * 切换界面的时候，由于外部的fragment完全不可见，
     * 导致ViewPager会自动销毁当前fragment，
     */
    override fun onDestroyView() {
        super.onDestroyView()
        //在最后的最后将 广播取消注册  动态注册 就不用 在manifest里注册了
        requireContext().unregisterReceiver(receiver)
        if (isBinder){
            requireContext().unbindService(connection) //解绑服务，其实放在这里不一定好
            isBinder = false
        }
        Log.d(TAG, "onDestroyView: 销毁视图FetalMove")
    }

    /**
     *主逻辑：
     */

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //动画效果
        scaleAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                // 动画开始时的操作
                clickCount.textSize =24.0f
            }

            override fun onAnimationEnd(animation: Animation) {
                // 动画结束时的操作
                clickCount.textSize = 24.0f
            }

            override fun onAnimationRepeat(animation: Animation) {
                // 动画重复时的操作
            }
        })

        //注册广播接收器
        val filter = IntentFilter("com.example.MY_CUSTOM_ACTION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireContext().registerReceiver(receiver, filter,Context.RECEIVER_EXPORTED)
        }else{
            requireContext().registerReceiver(receiver,filter)
        }

        //viewModel处理视图显示数据：
        viewModel.countdownModel.observe(viewLifecycleOwner){
            countdownTime.text = it.remainTime
            circlePercentView.setPercentTime(it.percentage)
        }
        viewModel.startModel.observe(viewLifecycleOwner){
            startTime.text = it.startTime
            //控制服务按钮的显示
            if (it.isStart){
                btnService.visibility= View.VISIBLE
                showToast("胎动+1")
                Log.d(TAG, "onCreateView: 开始胎动")
            }
            else{
                btnService.visibility= View.INVISIBLE
            }
        }
        viewModel.clickModel.observe(viewLifecycleOwner){
            //他会动，他来决定+1与否
            clickCount.text = it.toString()
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - viewModel.getLastClickTime()
            if(it !=0){ //为0不动
                if (elapsedTime > FIVE_MINUTES_IN_MILLIS) {
                    showToast("胎动+1")
                    Log.d(TAG, "onViewCreated: 点击次数$it")
                }else{
                    showToast("5分钟以内只算一次胎动哦！")
                }

            }

        }

        viewModel.countModel.observe(viewLifecycleOwner){
            circlePercentView.setTaiDongCount(it)
        }

        //获取广播
        btnitem.setOnClickListener {
            val a= TestDialog()
            val fragmentManager = requireActivity().supportFragmentManager
            a.show(fragmentManager, "MyDialog")
        }
        //获得了 展示 宫缩胎动小知识
        btnService.visibility=View.INVISIBLE

        btnStar.setOnClickListener {
            val scalegetAnim = getScaleAnimation()
            it.startAnimation(scalegetAnim)
            it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            viewModel.click()
        }

        btnStop.setOnClickListener {
            if (viewModel.getIsStart()) {
               //提示框：
                MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialogStyle)
                    .setTitle("本次胎动记录已完成")
                    .setMessage("开始时间：                               ${startTime.text}\n\n\n结束时间：                               ${getCurrent()}\n\n\n有效胎动：                                 ${viewModel.getCount()}\n\n\n点击次数:                                   ${clickCount.text}")
                    .setNegativeButton("") { dialog, which ->
                    }
                    .setPositiveButton("确定") { dialog: DialogInterface?, which: Int ->
                    }
                    .show()

                //数据归零：
                viewModel.clearAll()
                //进度归0,这个涉及进度状态的变化
                circlePercentView.replaceCurPercent()
                //按钮状态变化
                btnService.visibility = View.INVISIBLE

            }
        }

        btnService.setOnClickListener {
            //这就是一个简单的启动服务。
            startFloatingButtonService()
        }

        //初始化：
        if (viewModel.getIsStart()){
            btnService.visibility=View.VISIBLE //这个可视化
        }

    }

    /**
     * 启动悬浮窗
     */
    private fun startFloatingButtonService(){
        Log.d(TAG, "startFloatingButtonService: ${XuanfuService.isStarted()}")
        if (XuanfuService.isStarted()) return
        //申请权限
        if (!Settings.canDrawOverlays(requireActivity()))
        {
            Log.d(TAG, "startFloatingButtonService: 申请权限")
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:"+context?.packageName))
            startServiceLauncher.launch(intent)//起到了界面跳转的作用
        }
        else{
            Log.d(TAG, "startFloatingButtonService: 启动服务")
           val intent= Intent(requireActivity(), XuanfuService::class.java)
            lifecycleScope.launch {
                requireActivity().startService(intent) //启动服务
                delay(TimeUtils.SECOND*2)
                //延迟两秒等服务启动后再绑定服务，不然会出现先绑定服务，再启动服务的情况，就会报错
                if(!isBinder){
                    requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE) //绑定服务
                }

            }

        }
    }

    private fun showToast(message: String)= ToastUtils().showToast(requireContext(),message)

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.example.MY_CUSTOM_ACTION") {

                    btnStar.performClick()//实现点击按钮的效果
            }
        }
    }

}




