package com.exemplio.geapfitmobile.domain.usecase

import com.exemplio.geapfitmobile.data.service.ApiServicesImpl
import com.exemplio.geapfitmobile.domain.entity.CredentialModel
import com.exemplio.geapfitmobile.domain.entity.PasswordGrantRequest
import com.exemplio.geapfitmobile.domain.entity.Resultado
import com.exemplio.geapfitmobile.domain.repository.AuthRepository
import javax.inject.Inject

class Login @Inject constructor(private val authRepository: AuthRepository,
    private val apiService: ApiServicesImpl
) {
    suspend operator fun invoke(user: String, password: String): Resultado<Any?>? {
        if (user.contains("@hotmail.com")) {
            return null
        }
        val body = PasswordGrantRequest(
            email = user,
            password = password,
        )
        return apiService.passwordGrant(body)
    }
}