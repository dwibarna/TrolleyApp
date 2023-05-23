package com.sobarna.trolleyapp.data.source.local.room

import androidx.room.*
import com.sobarna.trolleyapp.data.source.local.entity.StoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {

    @Query("SELECT * FROM store")
    fun getAllStore(): Flow<List<StoreEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStoreWhenLogin(storeEntity: List<StoreEntity>)

    @Update
    suspend fun updateStore(storeEntity: StoreEntity)

    @Delete
    suspend fun deleteAll(storeEntity: List<StoreEntity>)
}