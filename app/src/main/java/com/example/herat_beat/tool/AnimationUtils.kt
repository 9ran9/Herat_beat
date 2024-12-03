package com.example.herat_beat.tool

import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation

object AnimationUtils {
    fun getScaleAnimation(): Animation {
        /**
         * 实例化 ScaleAnimation 主要是缩放效果
         *         参数：fromX-动画开始前，x坐标   toX-动画结束后x坐标
         *         fromY-动画开始前，Y坐标  toY-动画结束后Y坐标
         *         pivotXType - 为动画相对于物件的X坐标的参照物   pivotXValue - 值
         *         pivotYType - 为动画相对于物件的Y坐标的参照物   pivotYValue - 值
         */

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
}