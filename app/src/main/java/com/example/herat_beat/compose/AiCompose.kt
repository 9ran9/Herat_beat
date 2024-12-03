package com.example.herat_beat.compose

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.herat_beat.R


const val TAG = "AiCompose"

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AiCompose(
    taidong:String="0",
    gongsuo:String="0",
    forecastClick: () -> Int, //向上回调,在view域
    outcomeClick: () -> Unit, //向下回调,在view域
) {
    var imageMode by remember{ mutableIntStateOf(3) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column (modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
        ){
            //AI预警
            AIWarning()
            //两个Row
            TwoRow(taidong, gongsuo)

            //开始预测，封装逻辑,多次向上回调
            StartForecast(forecastClick){
                result ->
                imageMode = result //更新imageMode
            }
            //应该可以全局参数的

            Column(Modifier.weight(1f)){
                Spacer(modifier = Modifier.weight(0.8f))
                //预测结果：
                Surface(
                    Modifier.weight(2.2f)
                ) {
                    OutcomeForecaset(imageMode,outcomeClick)
                }
                Spacer(modifier = Modifier.weight(1f))
            }


        }

    }
}

@Composable
fun TwoRow(taidong: String, gongsuo: String) {
    Column(modifier = Modifier.padding(10.dp)){

        Row(modifier = Modifier.fillMaxWidth()
        )
        {
            // ImageView with id="@+id/imageView2"
            Image(
                painter = painterResource(id = R.drawable.blueee),
                contentDescription = null, // 请提供适当的内容描述
                modifier = Modifier
                    .size(24.dp, 24.dp)
                ,
                contentScale = ContentScale.Fit
            )
            Text(
                text = "每日推算胎动次数",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))//用权重把下面Box压到右边
            //end
            Box {
                Image(
                    painter = painterResource(id = R.drawable.bluevc),
                    contentDescription = null, // 请提供适当的内容描述
                    modifier = Modifier
                        .size(82.dp, 24.dp)
                    ,
                    contentScale = ContentScale.Fit
                ) // TextView with id="@+id/textView6"
                Text(
                    text = taidong,
                    color = colorResource(R.color.main_blue),
                    modifier = Modifier.align(Alignment.Center),

                    )
            }

        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
        )
        {
            Image(
                painter = painterResource(id = R.drawable.redddd),
                contentDescription = null, // 请提供适当的内容描述
                modifier = Modifier
                    .size(24.dp, 24.dp)
                ,
                contentScale = ContentScale.Fit
            )

            Text(
                text = "每小时平均宫缩间隔时间",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))//用权重把下面Box压到右边
            //end
            Box {
                Image(
                    painter = painterResource(id = R.drawable.redvc),
                    contentDescription = null, // 请提供适当的内容描述
                    modifier = Modifier
                        .size(82.dp, 24.dp)
                    ,
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = gongsuo,
                    color = colorResource(R.color.red_main),
                    modifier = Modifier.align(Alignment.Center),

                    )
            }

        }
    }
}

@Composable
fun AIWarning() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(top = 28.dp)
    ) {
        Text(
            text = "AI预警",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}


@Composable
fun OutcomeForecaset(imageMode:Int,outcomeClick: () -> Unit) {
    val change = remember {
        mutableStateOf(false)
    }
    val imageSize by animateDpAsState(
        targetValue = if (change.value) 55.dp else 50.dp,
        label = "图片放缩"
    )

    if (imageSize == 60.dp) {
        //如果用change的话，由于动画需要时间。所以就会出现change直接变两次，而size不变
        change.value = false //结束放缩，这样可以有效控制change的值
    }

    //用imageMode来跟踪图片实现重组：
    val imageId = when (imageMode) {
        0->R.drawable.wenti
        1->R.drawable.dontnow
        2->R.drawable.yyyyees
        else->R.drawable.notiong
    }

    Row{
        Image(
            painter = painterResource(id = R.drawable.something),
            contentDescription = null, // 请提供适当的内容描述
            modifier = Modifier
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                        //依据补的参数，直接抄
                    },
                    indication = null,//去掉波纹效果
                    onClick = {
                        outcomeClick()
                        change.value = true //开始放缩,防止重复点击
                    },
                )
                .size(imageSize, imageSize * (7 / 5))
                .padding(start = 20.dp)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Fit
        )
        Log.d(TAG, "AiCompose: imageId=$imageId")

        // ImageView with id="@+id/imageView4"
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null, // 请提供适当的内容描述
            modifier = Modifier,
            contentScale = ContentScale.Fit
        )
    }
}

/**
 * 开始预测模块，其中block用于向上传递预测结果
 */

@Composable
fun StartForecast(forecastClick: () -> Int,block:(Int)->Unit){

    var result by remember { mutableIntStateOf(3) }

    Image(
        painter = painterResource(id = R.drawable.predicttion),
        contentDescription = null, // 请提供适当的内容描述
        modifier = Modifier
            .size(450.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,//去掉波纹效果
                onClick = {
                    result = forecastClick()
                    block(result)  //用于向上传递预测结果
                }
            )
    )
}


@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    //能调试部分界面代码的功能真的逆天
    AiCompose(
        taidong = "0",
        gongsuo = "2",
        forecastClick = {
            Log.d(TAG, "预测回调:开始预测")
        },
        outcomeClick = {
            Log.d(TAG, "预测回调:预测结果")
        }
    )
}