package com.sobarna.trolleyapp.data.source.remote.network

import com.sobarna.trolleyapp.data.source.remote.response.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login/loginTest")
    suspend fun postLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse
}