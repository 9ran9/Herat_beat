package com.example.herat_beat.ui.forecast

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.herat_beat.R
import com.example.herat_beat.view.dialog.CustomProgressDialog
import com.example.herat_beat.view.dialog.ThirdDialog
import com.example.herat_beat.compose.AiCompose
import com.example.herat_beat.databases.DatabaseHelper
import com.example.herat_beat.tool.FormatUtils.getDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ForecastFragment : Fragment() {
    //数据
    private var count=0
    private var youxiao=0

    //视图
    private lateinit var composeView: ComposeView
    private var progressDialog: CustomProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view=inflater.inflate(R.layout.fragment_notifications, container, false)
        composeView=view.findViewById(R.id.composeView)//composeView控件实例化
        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //数据库初始化

        val list= DatabaseHelper().getAllItemsbyDate(getDate())
        var ttttt=1
        for (item in list)
        {
            ttttt++
            val minutes = convertTimeStringToMinutes(item.Jiange)
            count+=minutes
        }
        val avgtime=count/ttttt

        val liss= DatabaseHelper().getAllHomebydate(getDate())
        var sss=1
        for (i in liss)
        {
           youxiao+=i.frequency
            sss++
        }
        val avgtai=youxiao*12/sss

        //composeView控件内容
        composeView.setContent {
            AiCompose(
                taidong = avgtime.toString(),
                gongsuo = avgtai.toString(),
                forecastClick = {
                    progressDialog = CustomProgressDialog(requireContext(),R.style.progressDialog)
                    //设置不可点击外边取消动画
                    progressDialog!!.setCanceledOnTouchOutside(false)
                    //动画显示
                    progressDialog!!.show()
                    val coroutineScope = CoroutineScope(Dispatchers.Main)
                    // 在协程中延迟执行操作
                    coroutineScope.launch {
                        delay(1000) // 延迟 1 秒
                        progressDialog!!.cancel()
                        //也是传入compose
                    }
                    //作为反参数传入compose：
                    getpredict(avgtai,avgtime)//compose中调用具体的回调方法

                },
                outcomeClick = {
                    //compose没有view，含it的放缩逻辑写在里面
                    val a= ThirdDialog()
                    val fragmentManager = requireActivity().supportFragmentManager
                    a.show(fragmentManager, "MyDialog")
                }
            )
        }

    }

    private fun getpredict(avgTD: Int,avgGS: Int):Int
    {
        if (avgTD<10){
            return 0
        }
        if (avgGS<5)
            return 0
        if (avgTD>30&&avgGS>10)
        {
            return 2
        }
        return 1
    }

    private fun convertTimeStringToMinutes(timeString: String): Int {
        val char=timeString[0]
        val char2=timeString[1]
        return if (char.code ==0) char2.code
        else timeString.take(2).toInt()
    }
}