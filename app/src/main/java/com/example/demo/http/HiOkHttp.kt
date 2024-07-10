package com.example.demo.http

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

object HiOkHttp {
    private val BASE_URL = "https://baidu.com"
    val client  = OkHttpClient.Builder()
        .connectTimeout(10,TimeUnit.SECONDS)
        .readTimeout(10,TimeUnit.SECONDS)
        .writeTimeout(10,TimeUnit.SECONDS)
        .build()
    fun get(){
        Thread(Runnable {
            val request = Request.Builder()
                .url("$BASE_URL/")
                .build()
            val call  = client.newCall(request)
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("test","fail net")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.e("test","success net")
                    val body = response.body?.string()
                    println("get response $body")
                }
            })
        }).start()
    }

}