package com.example.demo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.demo.http.Email
import com.example.demo.http.EmailRequest
import com.example.demo.http.EmailResponse
import com.example.demo.http.LoginAPI
import com.example.demo.http.LoginRequest
import com.example.demo.http.LoginResponse
import com.example.demo.http.RetrofitTool
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Test : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoPlayerScreen("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4")
        }
        tryLoginTest("2039858744@qq.com", "yfyWwp")
    }

    private fun tryLoginTest(email: String, smscode: String) {
        RetrofitTool.create(LoginAPI::class.java)
            .tryLogin(LoginRequest(email, smscode))
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            Log.d("RetrofitTool", "Code: ${apiResponse.code}")
                            Log.d("RetrofitTool", "Message: ${apiResponse.message}")
                            Log.d("RetrofitTool", "Data: ${apiResponse.data}")
                        } else {
                            Log.d("RetrofitTool", "Response is null from tryLoginTest")
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun sendEmailRequest(email: String, ttl: Long) {
        RetrofitTool.create(Email::class.java)
            .sendEmail(EmailRequest(email, ttl))
            .enqueue(object : Callback<EmailResponse> {
                override fun onResponse(call: Call<EmailResponse>, response: Response<EmailResponse>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            Log.d("RetrofitTool", "Code: ${apiResponse.code}")
                            Log.d("RetrofitTool", "Message: ${apiResponse.message}")
                            Log.d("RetrofitTool", "Data: ${apiResponse.data}")
                        } else {
                            Log.d("RetrofitTool", "Response is null")
                        }
                    } else {
                        Log.d("RetrofitTool", "Request failed with status: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
}

@Composable
fun VideoPlayerScreen(videoUrl: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                this.player = exoPlayer
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

