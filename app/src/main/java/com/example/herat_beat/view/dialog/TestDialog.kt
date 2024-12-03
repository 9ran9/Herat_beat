package com.example.herat_beat.view.dialog

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import com.arc.fast.immersive.ImmersiveDialog
import com.arc.fast.immersive.ImmersiveDialogConfig
import com.example.herat_beat.R

/**
 * 第一步：Dialog改为继承ImmersiveDialog
 */
class TestDialog : ImmersiveDialog() {

    /**
     * 第二步：实现layoutId（Dialog的布局文件）
     * 注意：您不需要再自行实现onCreateView方法
     */
    override val layoutId: Int get() = R.layout.dialoggg

    /**
     * 第二步：实现immersiveDialogConfig（沉浸式配置），为简化配置，我们内置了三种常用配置：
     * 1. createFullScreenDialogConfig 全屏弹窗配置
     * 2. createBottomDialogConfig 底部弹窗配置
     * 3. createSoftInputAdjustResizeDialogConfig 带输入框的弹窗配置
     * 如果您有更多自定义需求，您可以自行创建自己的ImmersiveDialogConfig
     * 注意：您不需要再自行配置Dialog和Window，通过ImmersiveDialogConfig即可简单完成配置
     */
    override val immersiveDialogConfig
        get() = ImmersiveDialogConfig.createFullScreenDialogConfig().apply {
            height = WRAP_CONTENT // 可选：设置弹窗宽度
            width = WRAP_CONTENT // 可选：设置弹窗高度
            backgroundDimEnabled = false // 可选：禁用默认弹窗背景
            backgroundColor = 0x99000000.toInt() // 可选：设置弹窗背景的颜色
        }
    /**
     * 第三步：在onViewCreated实现您自己的业务逻辑
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}