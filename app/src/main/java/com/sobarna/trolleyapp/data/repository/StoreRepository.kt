package com.sobarna.trolleyapp.data.repository

import com.sobarna.trolleyapp.data.Resource
import com.sobarna.trolleyapp.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    fun getAllStore(): Flow<Resource<List<Store>>>
    fun insertStoreWhenLogin(username: String, password: String): Flow<Resource<List<Store>>>
    suspend fun deleteAll(stores: List<Store>)
    suspend fun updateStore(store: Store)
}

