package com.exemplio.geapfitmobile.view.home.screens.agenda

import com.exemplio.geapfitmobile.domain.entity.UserEntity


sealed class AgendaUiState {
    object Initial : LibraryUiState()
    object Loading : LibraryUiState()
    sealed class AgendaState
    object AgendaInitialState : AgendaState()
    object AgendaLoadingProductState : AgendaState()
    sealed class AgendaEvent
    object AgendaRefreshEvent : AgendaEvent()
    object AgendaInitEvent : AgendaEvent()
    data class AgendaLoadedProductState(val agenda: List<Any>) : AgendaState()
    data class AgendaErrorProductState(val errorMessage: String = "Test screen") : AgendaState()
    data class Loaded(val usuarios: List<UserEntity>) : LibraryUiState()
    data class Error(val message: String) : LibraryUiState()
}
