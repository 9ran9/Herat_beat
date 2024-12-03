package com.example.herat_beat.tool

import android.content.Context
import android.widget.Toast

class ToastUtils {
     fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}