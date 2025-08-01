package com.exemplio.geapfitmobile.data.repository

import com.exemplio.geapfitmobile.data.service.HttpServiceImpl
import android.util.Log
import com.exemplio.geapfitmobile.data.response.toDomain
import com.exemplio.geapfitmobile.data.service.IsOnlineProvider
import com.exemplio.geapfitmobile.domain.entity.UserEntity
import com.exemplio.geapfitmobile.domain.repository.AuthRepository
import javax.inject.Inject
import com.exemplio.geapfitmobile.domain.repository.ApiRepository

class AuthRepositoryImpl @Inject constructor(val api: ApiRepository, private val httpService: HttpServiceImpl, private val isOnlineProvider: IsOnlineProvider
) : AuthRepository {

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
