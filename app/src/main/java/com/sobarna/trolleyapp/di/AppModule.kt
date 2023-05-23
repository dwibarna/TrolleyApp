package com.sobarna.trolleyapp.di

import com.sobarna.trolleyapp.domain.usecase.StoreCaseImpl
import com.sobarna.trolleyapp.domain.usecase.StoreUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun provideStoryUseCase(useCase: StoreCaseImpl): StoreUseCase
}