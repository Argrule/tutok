package com.example.demo.http

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class VideoResponse(
    val code: Int,
    val message: String,
    val data: List<VideoData>
)

data class VideoData(
    val id: Long,
    val title: String,
    val url: String,
    val cover: String,
    val description: String,
    val userid: Long,
    val username: String,
    val userAvatar: String,
    val likeCount: Int,
    val commentCount: Int,
//    val status: Int,
//    val createTime: Long,
    val updateTime: Long,
)

interface Video {
    @GET("video")
    fun getVideo(
        @Query("latestTime") latestTime: Long,
        @Query("number") number: Int
    ): Call<VideoResponse>
}