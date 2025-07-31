package com.exemplio.geapfitmobile.view.home.screens.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exemplio.geapfitmobile.data.repository.ClientRepositoryImpl
import com.exemplio.geapfitmobile.domain.usecase.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(val repository: Client) : ViewModel() {

    private val _uiState = MutableStateFlow<ClientUiState>(ClientUiState.Initial)
    val uiState: StateFlow<ClientUiState> = _uiState.asStateFlow()

    init {
        initClients()
    }

    fun initClients() {
        _uiState.value = ClientUiState.Loading
        viewModelScope.launch {
            try {
//                val clients = repository.invoke()
//                _uiState.value = ClientUiState.Loaded(clients)
            } catch (e: Exception) {
                _uiState.value = ClientUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun refresh() {
        initClients()
    }

}