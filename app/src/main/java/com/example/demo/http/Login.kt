package com.example.demo.http

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest (
    val email: String,
    val smscode:String
)
data class LoginResponse(
    val code: Int,
    val message: String,
    val data: Data
)

data class Data(
    val token: String,
    val user: User
)

data class User(
    val id: Int,
    val name: String,
    val avatar: String,
    val sex: String,
    val massage: String?, // 因为可能为 null，所以用 String? 表示
    val followCount: Int,
    val followedCount: Int
)

interface LoginAPI {
    @POST("user/login")
    fun tryLogin(@Body request: LoginRequest):Call<LoginResponse>
}