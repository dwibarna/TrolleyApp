package com.sobarna.trolleyapp.ui.maplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sobarna.trolleyapp.domain.usecase.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListStoreViewModel @Inject constructor(private val useCase: StoreUseCase): ViewModel() {

    fun getListStore() = useCase.getAllStore().asLiveData()
}