package com.sobarna.trolleyapp.di

import com.sobarna.trolleyapp.data.repository.StoreRepository
import com.sobarna.trolleyapp.data.repository.StoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        DatabaseModule::class,
        NetworkModule::class
    ]
)
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(repository: StoreRepositoryImpl): StoreRepository
}