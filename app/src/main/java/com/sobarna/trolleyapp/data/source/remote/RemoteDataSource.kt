package com.sobarna.trolleyapp.data.source.remote

import android.util.Log
import com.sobarna.trolleyapp.data.source.remote.network.ApiResponse
import com.sobarna.trolleyapp.data.source.remote.network.ApiService
import com.sobarna.trolleyapp.data.source.remote.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun postLogin(username: String, password: String): Flow<ApiResponse<LoginResponse>> =
        flow {
            try {
                apiService.postLogin(username, password).let {
                    emit(
                        if (it.status != "success")
                            ApiResponse.Error(it.message ?: "")
                        else
                            ApiResponse.Success(it)
                    )
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message ?: ""))
                Log.d(javaClass.name, e.toString())
            }
        }.flowOn(Dispatchers.IO)
}