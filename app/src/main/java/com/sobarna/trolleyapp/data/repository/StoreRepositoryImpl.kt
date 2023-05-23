package com.sobarna.trolleyapp.data.repository

import com.sobarna.trolleyapp.data.NetworkBoundResource
import com.sobarna.trolleyapp.data.Resource
import com.sobarna.trolleyapp.data.source.local.LocalDataSource
import com.sobarna.trolleyapp.data.source.remote.RemoteDataSource
import com.sobarna.trolleyapp.data.source.remote.network.ApiResponse
import com.sobarna.trolleyapp.data.source.remote.response.LoginResponse
import com.sobarna.trolleyapp.domain.model.Store
import com.sobarna.trolleyapp.util.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : StoreRepository {

    override fun getAllStore(): Flow<Resource<List<Store>>> = flow {
        emit(Resource.Loading())
        try {
            emitAll(
                localDataSource.getAllStore().map {
                    Resource.Success(DataMapper.mapperListEntityToListDomain(it))
                }
            )
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message))
        }
    }

    override fun insertStoreWhenLogin(
        username: String,
        password: String
    ): Flow<Resource<List<Store>>> {
        return object : NetworkBoundResource<List<Store>, LoginResponse>() {
            override fun loadFromDatabase(): Flow<List<Store>> {
                return localDataSource.getAllStore().map {
                    DataMapper.mapperListEntityToListDomain(it)
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<LoginResponse>> {
                return remoteDataSource.postLogin(username, password)
            }

            override suspend fun saveCallResult(data: LoginResponse) {
                data.let {
                    localDataSource.insertStoreWhenLogin(
                        DataMapper.mapperResponseToEntity(it)
                    )
                }
            }
        }.asFlow()
    }

    override suspend fun deleteAll(stores: List<Store>) {
        return localDataSource.deleteAll(DataMapper.mapperListDomainToEntity(stores))
    }

    override suspend fun updateStore(store: Store) {
        return localDataSource.updateStore(DataMapper.mapperDomainToEntity(store))
    }
}


