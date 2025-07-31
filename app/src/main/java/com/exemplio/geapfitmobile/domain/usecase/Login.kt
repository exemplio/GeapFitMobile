package com.exemplio.geapfitmobile.domain.usecase

import com.example.geapfit.models.CredentialModel
import com.example.geapfit.models.PasswordGrantRequest
import com.exemplio.geapfitmobile.data.datasource.api.ApiServices2
import com.exemplio.geapfitmobile.domain.entity.UserEntity
import com.exemplio.geapfitmobile.domain.repository.AuthRepository
import com.geapfit.services.ApiServices
import com.geapfit.services.http.Result2
import javax.inject.Inject

class Login @Inject constructor(private val authRepository: AuthRepository,
    private val apiService: ApiServices2
) {
    suspend operator fun invoke(user: String, password: String): CredentialModel? {
        if (user.contains("@hotmail.com")) {
            return null
        }
        val body = PasswordGrantRequest(
            username = user,
            password = password,
            clientId = "your_client_id",
            clientSecret = "your_client_secret",
            scope = "your_scope"
        )
        return apiService.passwordGrant("",body)
    }
}