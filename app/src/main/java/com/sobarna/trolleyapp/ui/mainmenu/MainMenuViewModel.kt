package com.sobarna.trolleyapp.ui.mainmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sobarna.trolleyapp.domain.model.Store
import com.sobarna.trolleyapp.domain.usecase.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(private val useCase: StoreUseCase): ViewModel() {
    fun getAllStore() = useCase.getAllStore().asLiveData()

    fun logOut(store: List<Store>) = viewModelScope.launch { useCase.deleteAll(store) }
}