package com.sobarna.trolleyapp.di

import android.content.Context
import androidx.room.Room
import com.sobarna.trolleyapp.data.source.local.room.StoreDao
import com.sobarna.trolleyapp.data.source.local.room.StoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): StoreDatabase =
        Room.databaseBuilder(
            context = context,
            klass = StoreDatabase::class.java,
            name = "store.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideStoreDao(database: StoreDatabase): StoreDao = database.storeDao()
}