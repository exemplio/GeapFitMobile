package com.exemplio.geapfitmobile.view.home.screens.client

import com.exemplio.geapfitmobile.domain.entity.UserEntity


sealed class ChatsUiState {
    object Initial : ChatsUiState()
    object Loading : ChatsUiState()
    data class Loaded(val usuarios: List<UserEntity>) : ChatsUiState()
    data class Error(val message: String) : ChatsUiState()
}
