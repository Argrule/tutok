package com.example.demo

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
//        val textview = TextView(this)
//        textview.text = "test a try"
//        textview.gravity = Gravity.CENTER
        setContentView(R.layout.fragment_blank)


//        sendEmailRequest("1.2@qq.com", 0)
        tryLoginTest("2039858744@qq.com","yfyWwp")
        
//        val bf = BlankFragment()
//        val bundle = Bundle()
//        bundle.putInt("key_int", 100)
//        bf.arguments = bundle
//        val ft = supportFragmentManager.beginTransaction()
//        ft.add(R.id.container, bf)
//        ft.commitAllowingStateLoss()
    }

    fun tryLoginTest(email: String, smscode: String) {
        RetrofitTool.create(LoginAPI::class.java)
            .tryLogin(LoginRequest(email,smscode))
            .enqueue(object :Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
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
                    t.printStackTrace() // 打印异常信息
                }
            })

    }

    fun sendEmailRequest(email: String, ttl: Long) {
        RetrofitTool.create(Email::class.java)
            .sendEmail(EmailRequest(email, ttl))
            .enqueue(object : Callback<EmailResponse> {
                override fun onResponse(
                    call: Call<EmailResponse>, response: Response<EmailResponse>
                ) {
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