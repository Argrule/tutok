package com.example.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.demo.http.HiOkHttp
import com.example.demo.ui.login.LoginActivity
import com.example.demo.ui.theme.DemoTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        enableEdgeToEdge()
//        setContent {
//            DemoTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android_demo",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }

        Log.d("TAG", "onCreate: test")
        if (!isLoggedIn()) {
            redirectToLoginActivity()
            return  // 重要：如果跳转到登录页，不要执行下面的代码
        }
//        HiOkHttp.get()
    }
    fun onLoginClick(view: View?) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    fun onTestClick(view: View?){
        val intent = Intent(this, Test::class.java)
        startActivity(intent)
    }
    private fun isLoggedIn(): Boolean {
        // 实现检查用户登录状态的逻辑
        // 这里只是一个示例，您需要根据实际情况实现
        return true
    }

    private fun redirectToLoginActivity() {
        val intent = Intent(this, Test::class.java)
        // 如果MainActivity是启动页，且用户未登录，需要关闭它
        startActivity(intent)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemoTheme {
        Greeting("Android111")
    }
}