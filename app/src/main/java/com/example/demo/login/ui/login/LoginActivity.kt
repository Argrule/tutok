package com.example.demo.login.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import androidx.annotation.StringRes
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.example.demo.databinding.ActivityLoginBinding
import com.example.demo.R // 导入资源文件
import com.example.demo.http.Email
import com.example.demo.http.EmailRequest
import com.example.demo.http.EmailResponse
import com.example.demo.http.RetrofitTool
import retrofit2.Callback

class LoginActivity : ComponentActivity() {

    private lateinit var loginViewModel: LoginViewModel // 声明登录视图模型
    private lateinit var binding: ActivityLoginBinding // 声明绑定对象，用于访问布局中的视图
    private var markTime = 0L // 记录上次点击的时间(服务器返回的时间戳)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // 调用父类的onCreate方法
        Log.e("TEST", "onCreate: login launch")
        binding = ActivityLoginBinding.inflate(layoutInflater) // 使用DataBinding绑定布局
        setContentView(binding.root) // 设置布局的根视图

        // 从绑定对象中获取UI组件
        val username = binding.username
        val password = binding.password
        val getVerificationCode = binding.getVerificationCode!!
        val login = binding.login
        val loading = binding.loading

        // 创建LoginViewModel实例
        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        // 观察登录表单状态的LiveData，更新UI组件状态
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // 仅当用户名和密码有效时启用登录按钮
            login.isEnabled = loginState.isDataValid

            // 根据 isDataValid 的值来设置按钮的颜色
            if (loginState.isDataValid) {
                // 如果表单数据有效，设置一个积极的颜色，例如粉色
                login.setBackgroundColor(ContextCompat.getColor(this, R.color.pink))
                login.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                // 如果表单数据无效，设置一个消极的颜色，例如灰色
                login.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
                login.setTextColor(ContextCompat.getColor(this, R.color.gray_txt))
            }
            // 设置用户名和密码的错误提示
            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        // 观察登录结果的LiveData，处理登录成功或失败
        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE // 隐藏加载指示器
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error) // 显示登录失败的提示
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success) // 更新UI以反映登录成功
            }
            setResult(Activity.RESULT_OK) // 设置结果码

            // 登录成功后关闭登录Activity
            finish()
        })

        // 监听用户名EditText文本变化，更新登录数据
        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(), password.text.toString()
            )
        }

        // 监听密码EditText文本变化和软键盘的“完成”按钮，更新登录数据
        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(), password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> // 当软键盘的“完成”按钮被点击
                        loginViewModel.login(
                            username.text.toString(), password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener { // 为登录按钮设置点击事件
                loading.visibility = View.VISIBLE // 显示加载指示器
                loginViewModel.login(username.text.toString(), password.text.toString()) // 执行登录
            }
        }
        getVerificationCode.setOnClickListener {
            val email = username.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, "请输入邮箱地址", Toast.LENGTH_SHORT).show()
            } else {
                // sendVerificationCodeRequest
                Log.d("test", "before : $markTime")
                RetrofitTool.create(Email::class.java)
                    .sendEmail(EmailRequest(email, markTime))
                    .enqueue(object : Callback<EmailResponse> {
                        override fun onResponse(
                            call: retrofit2.Call<EmailResponse>,
                            response: retrofit2.Response<EmailResponse>
                        ) {
                            if (response.isSuccessful) {
                                val apiResponse = response.body()
                                if (apiResponse != null) {
                                    println("Code: ${apiResponse.code}")
                                    println("Message: ${apiResponse.message}")
                                    println("Data: ${apiResponse.data}")
                                    if (apiResponse.code != 0) {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            apiResponse.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return
                                    }
                                    markTime = apiResponse.data
                                    Log.d("test", "onResponse: $markTime")
                                    // startCountDown
                                    getVerificationCode.isEnabled = false
                                    object : CountDownTimer(60000, 1000) {
                                        @SuppressLint("SetTextI18n")
                                        override fun onTick(millisUntilFinished: Long) {
                                            getVerificationCode.text =
                                                "${millisUntilFinished / 1000}秒后重新获取"
                                        }

                                        @SuppressLint("SetTextI18n")
                                        override fun onFinish() {
                                            getVerificationCode.text = "获取验证码"
                                            getVerificationCode.isEnabled = true
                                        }
                                    }.start()
                                } else {
                                    println("Response is null from tryLoginTest")
                                }
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<EmailResponse>, t: Throwable) {
                            t.printStackTrace() // 打印异常信息
                        }
                    })


            }
        }
    }

    // 更新UI以反映用户登录成功
    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) // 获取字符串资源
        val displayName = model.displayName // 获取登录用户的显示名称
        // 显示欢迎信息的Toast提示
        Toast.makeText(
            applicationContext, "$welcome $displayName", Toast.LENGTH_LONG
        ).show()
    }

    // 显示登录失败的提示
    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * 扩展函数，简化为EditText组件设置afterTextChanged动作
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString()) // 当文本变化后调用传入的函数
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}