package com.sobarna.trolleyapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobarna.trolleyapp.domain.model.Store
import com.sobarna.trolleyapp.domain.usecase.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreDetailViewModel @Inject constructor(private val useCase: StoreUseCase) : ViewModel() {

    fun updateStore(store: Store) = viewModelScope.launch {
        useCase.updateStore(store)
    }
}