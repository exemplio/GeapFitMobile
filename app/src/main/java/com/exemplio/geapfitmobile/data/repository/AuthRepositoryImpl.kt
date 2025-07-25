package com.exemplio.geapfitmobile.data.repository

import android.util.Log
import com.exemplio.geapfitmobile.data.datasource.api.ApiServices
import com.exemplio.geapfitmobile.data.response.UserResponse
import com.exemplio.geapfitmobile.data.response.toDomain
import com.exemplio.geapfitmobile.domain.entity.UserEntity
import com.exemplio.geapfitmobile.domain.repository.AuthRepository
import com.exemplio.geapfitmobile.domain.usecase.Login
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(val api: ApiServices) : AuthRepository {

    override suspend fun doLogin(user: String, password: String): List<UserEntity> {
        val response = try {
            api.doLogin()
        } catch (e: Exception) {
            Log.i("DOLOGIN ERROR", "$e")
            listOf()
        }
        return response.map { it.toDomain() }
    }
}
