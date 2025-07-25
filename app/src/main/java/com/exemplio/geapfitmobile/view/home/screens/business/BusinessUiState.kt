package com.exemplio.geapfitmobile.view.home.screens.agenda

import com.exemplio.geapfitmobile.domain.entity.UserEntity


sealed class BusinessUiState {
    object Initial : BusinessUiState()
    object Loading : BusinessUiState()
    sealed class BusinessState
    object BusinessInitialState : BusinessState()
    object BusinessLoadingProductState : BusinessState()
    sealed class BusinessEvent
    object BusinessRefreshEvent : BusinessEvent()
    object BusinessInitEvent : BusinessEvent()
    data class BusinessLoadedProductState(val agenda: List<Any>) : BusinessState()
    data class BusinessErrorProductState(val errorMessage: String = "Test screen") : BusinessState()
    data class Loaded(val usuarios: List<UserEntity>) : BusinessUiState()
    data class Error(val message: String) : BusinessUiState()
}
