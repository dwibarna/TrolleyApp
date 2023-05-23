package com.sobarna.trolleyapp.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sobarna.trolleyapp.data.source.local.entity.StoreEntity

@Database(
    entities = [StoreEntity::class],
    version = 2,
    exportSchema = false
)
abstract class StoreDatabase: RoomDatabase() {
    abstract fun storeDao(): StoreDao
}