package com.sobarna.trolleyapp.domain.usecase

import com.sobarna.trolleyapp.data.Resource
import com.sobarna.trolleyapp.data.repository.StoreRepository
import com.sobarna.trolleyapp.domain.model.Store
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoreCaseImpl @Inject constructor(private val repository: StoreRepository): StoreUseCase {

    override fun getAllStore(): Flow<Resource<List<Store>>> {
        return repository.getAllStore()
    }

    override fun insertStoreWhenLogin(
        username: String,
        password: String
    ): Flow<Resource<List<Store>>> {
        return repository.insertStoreWhenLogin(username, password)
    }

    override suspend fun deleteAll(store: List<Store>) {
        return repository.deleteAll(store)
    }

    override suspend fun updateStore(store: Store) {
        return repository.updateStore(store)
    }
}