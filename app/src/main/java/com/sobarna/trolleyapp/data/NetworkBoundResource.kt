package com.sobarna.trolleyapp.data

import com.sobarna.trolleyapp.data.source.remote.network.ApiResponse
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {

    private var result: Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        createCall().first().let {
            when (it) {
                is ApiResponse.Error -> {
                    onFetchFailed()
                    emit(Resource.Error(message = it.error))
                }
                is ApiResponse.Success -> {
                    saveCallResult(it.data)
                    emitAll(loadFromDatabase().map { value ->
                        Resource.Success(value)
                    })
                }
            }
        }
    }

    protected open fun onFetchFailed() {}
    protected abstract fun loadFromDatabase(): Flow<ResultType>
    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>
    protected abstract suspend fun saveCallResult(data: RequestType)
    fun asFlow(): Flow<Resource<ResultType>> = result
}