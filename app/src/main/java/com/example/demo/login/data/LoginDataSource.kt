package com.example.demo.login.data

import com.example.demo.MyApplication
import com.example.demo.login.data.model.LoggedInUser
import com.example.demo.http.LoginAPI
import com.example.demo.http.LoginRequest
import com.example.demo.http.LoginResponse
import com.example.demo.http.RetrofitTool
import com.example.demo.util.ShareTool
import java.io.IOException
import java.util.UUID

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            RetrofitTool.create(LoginAPI::class.java)
                .tryLogin(LoginRequest(username, password))
                .enqueue(object : retrofit2.Callback<LoginResponse> {
                    override fun onResponse(
                        call: retrofit2.Call<LoginResponse>,
                        response: retrofit2.Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            if (apiResponse != null) {
                                println("Code: ${apiResponse.code}")
                                println("Message: ${apiResponse.message}")
                                println("Data: ${apiResponse.data}")
                                // handle loggedInUser authentication
                                ShareTool.saveString(MyApplication.context, "token", apiResponse.data.token)
                            } else {
                                println("Response is null from tryLoginTest")
                            }
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<LoginResponse>,
                        t: Throwable
                    ) {
                        t.printStackTrace() // 打印异常信息
                    }
                })

            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}