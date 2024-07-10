package com.example.demo.http

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class EmailRequest(
    val email: String,
    val ttl: Long
)
data class EmailResponse(
    val code: Int,
    val message: String,
    val data: Long
)
interface Email {
    @POST("common/email")
    fun sendEmail(@Body request: EmailRequest): Call<EmailResponse>
}