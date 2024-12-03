package com.example.herat_beat.ui.record

import TimeRecord
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.herat_beat.databases.DatabaseHelper
import com.example.herat_beat.R
import com.example.herat_beat.view.dialog.Mydialogone
import com.example.herat_beat.view.dialog.secondDialog
import com.example.herat_beat.tool.AnimationUtils.getScaleAnimation
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

//contraction:宫缩
class Contraction : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TimeRecordAdapter
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var textView4: TextView
    private lateinit var handler:android.os.Handler
    private lateinit var runnable: Runnable
    private lateinit var btnitem:ImageButton
    private lateinit var str1:String
    private lateinit var str2:String
    private lateinit var str3:String
    var lastCurrentTime:Long=0
    private  var lastsecond :Int =0
    private var seconds: Int = 0
    private var  isstarted=false

    private lateinit var dataList :ArrayList<TimeRecord>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_gongsuo,container,false)
        recyclerView = view.findViewById(R.id.recyclerView)
        // Inflate the layout for this fragment
        // 创建布局管理器和适配器
        val layoutManager = LinearLayoutManager(requireContext())
        dataList= ArrayList()

        adapter = TimeRecordAdapter(dataList)
        // 设置 RecyclerView 的布局管理器和适配器\
        btnitem=view.findViewById(R.id.imageButton2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        textView1=view.findViewById(R.id.textView11)
        textView2=view.findViewById(R.id.textView22)
        textView3=view.findViewById(R.id.textView33)
        textView4=view.findViewById(R.id.textView44)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layout=view.findViewById<ConstraintLayout>(R.id.constraint_list)
        layout.visibility=View.INVISIBLE
       val btn1= view.findViewById<ImageButton>(R.id.imageButton)
        loadItemsFromDatabase(requireContext())

        btnitem.setOnClickListener {
            val a= secondDialog()
            val fragmentManager = requireActivity().supportFragmentManager
            a.show(fragmentManager, "MyDialog")
        }

        adapter.setOnItemClickListener(object : TimeRecordAdapter.OnItemClickListener {
            override fun onItemClick(itemID: Int) {
                showDialog(itemID)
            }
        })
        recyclerView.setAdapter(adapter);
        recyclerView.adapter = adapter

        //由于宫缩会持续20s以上，做一个时间检测
        var startTime = 0L
        btn1.setOnClickListener {
            val scalegetAnim = getScaleAnimation()
            it.startAnimation(scalegetAnim)
            it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            if (isstarted==false){
                startTime = System.currentTimeMillis()
                btn1.setImageResource(R.drawable.get_one)
                seconds=0
                isstarted=true
                layout.visibility=View.VISIBLE
                val cha= getCurrent()
                val minutes = TimeUnit.MILLISECONDS.toMinutes(cha)
                val secondsw = TimeUnit.MILLISECONDS.toSeconds(cha) % 60
                val timeString = String.format("%02d'%02d\'\'", minutes, secondsw)
                if(lastCurrentTime==0L){
                    textView3.text="30'00''"
                    str3= textView3.text.toString()
                }else
                { textView3.text = timeString
                    str3=timeString
                }
                setCurrentTime()
                // 在 Activity 或 Fragment 中启动计时器
                val countdownMillis: Long = 180000 // 正计时的总毫秒数
                val timer = Timer(countdownMillis)
                timer.start()

                // 开始计时 然后把东西加进去
                handler = android.os.Handler()
                runnable = object : Runnable {
                    override fun run() {
                        seconds++
                        val minutes = seconds / 60
                        val secondsDisplay = seconds % 60
                        val timeSstring = String.format("%02d'%02d\'\'", minutes, secondsDisplay)
                        textView2.text = timeSstring
                        str2=timeSstring
                        handler.postDelayed(this, 1000)
                    }
                }

                handler.postDelayed(runnable, 500)

            }
            else
            {
                btn1.setImageResource(R.drawable.starttt)
                val curTime = System.currentTimeMillis()
                if (curTime-startTime>=20*1000L){
                    val jiange=getCurrent()
                    println(jiange)
                    lastsecond= seconds
                    val minutes = lastsecond / 60
                    val secondsDisplay = lastsecond % 60
                    val timeString = String.format("%02d'%02d''", minutes, secondsDisplay)
                    //把数据添加到数据库：
                    additem(str1, str2, str3) //点块了str2，str3可能未被初始化

//                textView222.text=timeString
                }else{
                    Toast.makeText(requireContext(), "未到20s不算宫缩哦", Toast.LENGTH_SHORT).show()
                }
                //停止宫缩记录
                handler?.removeCallbacksAndMessages(null)
                layout.visibility=View.INVISIBLE
                isstarted=false

            }
        }

    }
    private fun additem(string1:String,string2:String,string3:String,string4:String="点击选择"){
        val newDataItem = TimeRecord(0,string1, string2, string3, string4)
        //无论 id是多少 数据库那里自己会帮你搞好 插入为id+1
        dataList.add(newDataItem)
        adapter.notifyDataSetChanged()
        DatabaseHelper().insertItem(newDataItem)
    }
    fun getCurrent():Long{
        return  Calendar.getInstance().time.time-lastCurrentTime
    }
    fun loadItemsFromDatabase(context: Context){
        dataList.clear()
        DatabaseHelper().getAllItems()?.let { dataList.addAll(it) }
        adapter.notifyDataSetChanged()
        Log.d("load","llllload")
    }
    fun setCurrentTime(){
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        textView1.text= dateFormat.format(calendar.getTime())
        str1= textView1.text.toString()
       lastCurrentTime= calendar.getTime().time
    }
    fun showDialog(itemID: Int){
        val items = arrayOf("1级", "2级", "3级", "4级","5级")
        val dialog = Mydialogone.newInstance("请选择宫缩疼痛等级", items, -1, false)
        dialog.setOnPositiveButtonClickListener {
            // 处理确定按钮点击事件
            if (dialog.getCheckedItem() != -1) {
                val selectedOption = items[dialog.getCheckedItem()]
                Log.d("dialog","$selectedOption") //string
                DatabaseHelper().updateItem(itemID,selectedOption)
                reloadData()
                adapter.notifyDataSetChanged()
                // 其他逻辑处理
            }
        }
        val fragmentManager = requireActivity().supportFragmentManager
        dialog.show(fragmentManager, "MyDialog")
    }

    @SuppressLint("SuspiciousIndentation")
    fun  reloadData() {
        // 执行数据库读取操作，获取最新的数据
        val newDataList = DatabaseHelper().getAllItems()// 从数据库中读取数据
            dataList.clear()
        dataList.addAll(newDataList)
        // 通知适配器数据已更改
    }
}

