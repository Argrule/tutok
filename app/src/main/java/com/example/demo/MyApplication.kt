package com.example.demo

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
            private set

        val context: Context
            get() = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
