package com.example.herat_beat.ui.record

import android.os.CountDownTimer

class Timer(countdownMillis: Long) : CountDownTimer(countdownMillis, 1000) {
    override fun onTick(millisUntilFinished: Long) {
        val seconds = millisUntilFinished / 1000
    }

    override fun onFinish() {
    }
}

