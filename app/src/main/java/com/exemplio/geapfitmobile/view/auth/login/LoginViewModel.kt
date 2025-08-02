package com.exemplio.geapfitmobile.view.auth.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exemplio.geapfitmobile.domain.entity.CredentialModel
import com.exemplio.geapfitmobile.domain.entity.Resultado
import com.exemplio.geapfitmobile.domain.usecase.Login
import com.geapfit.utils.translate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val login: Login) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState
    fun onEmailChanged(email: String) {
        _uiState.update { state ->
            state.copy(
                email = email,
            )
        }
        verifyLogin()
    }

    fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
        verifyLogin()
    }

    fun onClickSelected() {
        loadingState(true)
        viewModelScope.launch(Dispatchers.IO) {
            val response = login(_uiState.value.email, _uiState.value.password)
            withContext(Dispatchers.Main) {
                if (response != null) {
                    if (response.error!= null) {
                        if (response.error!=200){
                            Log.i("LOGIN", "ERROR")
                            _uiState.update { it.copy(errorMessage = translate(response.errorMessage), errorCode = 202) }
                        }else{
                            _uiState.update { it.copy(isUserLogged = false) }
                        }
                    }
                }else{
                    Log.i("LOGIN", "ERROR")
                }
                loadingState(false)
            }
        }
    }

    private fun loadingState(isLoading: Boolean){
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    private fun verifyLogin() {
        val enabledLogin =
            isEmailValid(_uiState.value.email) && isPasswordValid(_uiState.value.password)
        _uiState.update {
            it.copy(isLoginEnabled = enabledLogin)
        }
    }

    private fun isEmailValid(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isPasswordValid(password: String): Boolean = password.length >= 6
}

data class LoginUiState(
    val email: String = "ricardojosemolin@gmail.com",
    val password: String = "302211be",
    val isLoading: Boolean = false,
    val isLoginEnabled: Boolean = true,
    val isUserLogged:Boolean = false,
    var errorCode:Int? = null,
    var errorMessage:String? = null
)

