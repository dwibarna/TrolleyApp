package com.sobarna.trolleyapp.data.source.local

import com.sobarna.trolleyapp.data.source.local.entity.StoreEntity
import com.sobarna.trolleyapp.data.source.local.room.StoreDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val dao: StoreDao) {

    fun  getAllStore():Flow<List<StoreEntity>> = dao.getAllStore()

    suspend fun insertStoreWhenLogin(data:List<StoreEntity>) = dao.insertStoreWhenLogin(data)

    suspend fun updateStore(entity: StoreEntity) = dao.updateStore(entity)

    suspend fun deleteAll(storeEntity: List<StoreEntity>) = dao.deleteAll(storeEntity)
}