package com.exemplio.geapfitmobile.view.home.screens.agenda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exemplio.geapfitmobile.data.repository.ClientRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(val repository: ClientRepositoryImpl) : ViewModel() {

    private val _uiState = MutableStateFlow<LibraryUiState>(LibraryUiState.Initial)
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        initClients()
    }

    fun initClients() {
        _uiState.value = LibraryUiState.Loading
        viewModelScope.launch {
            try {
                val clients = repository.getClients()
                _uiState.value = LibraryUiState.Loaded(clients)
            } catch (e: Exception) {
                _uiState.value = LibraryUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun refresh() {
        initClients()
    }

}