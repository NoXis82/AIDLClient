package ru.example.apiserver

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SubService : Service() {

    override fun onBind(intent: Intent): IBinder {
        return object : ISubService.Stub() {
            override fun sub(firstNum: Double, secondNum: Double): Double {
                return firstNum + secondNum
            }
        }
    }
}