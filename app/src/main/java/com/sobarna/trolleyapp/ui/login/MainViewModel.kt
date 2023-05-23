package com.sobarna.trolleyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sobarna.trolleyapp.domain.usecase.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val useCase: StoreUseCase) : ViewModel() {

    fun postLogin(username: String, password: String) =
        useCase.insertStoreWhenLogin(username, password).asLiveData()


    fun getAllData() = useCase.getAllStore().asLiveData()
}


