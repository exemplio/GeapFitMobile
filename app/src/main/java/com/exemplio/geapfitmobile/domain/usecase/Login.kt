package com.exemplio.geapfitmobile.domain.usecase

import com.example.geapfit.models.CredentialModel
import com.exemplio.geapfitmobile.data.service.ApiServicesImpl
import com.exemplio.geapfitmobile.domain.repository.AuthRepository
import com.geapfit.services.http.Result2
import javax.inject.Inject

class Login @Inject constructor(private val authRepository: AuthRepository,
    private val apiService: ApiServicesImpl
) {
    suspend operator fun invoke(user: String, password: String): Result<Any>? {
        if (user.contains("@hotmail.com")) {
            return null
        }
        val body = CredentialModel(
            accessToken = "",
            tokenType = "",
            expiresIn = 0,
            refreshToken = "",
        )
        return apiService.resendSign(body)
    }
}