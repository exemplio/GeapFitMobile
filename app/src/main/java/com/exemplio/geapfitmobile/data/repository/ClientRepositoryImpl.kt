package com.exemplio.geapfitmobile.data.repository

import ClientRepository
import android.util.Log
import com.exemplio.geapfitmobile.data.datasource.api.ApiServices
import com.exemplio.geapfitmobile.data.response.toDomain
import com.exemplio.geapfitmobile.domain.entity.UserEntity
import com.exemplio.geapfitmobile.domain.repository.ClientApiService
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(val api: ApiServices) : ClientRepository {

    override suspend fun getClients(): List<UserEntity> {
        val response = try {
            api.doLogin()
        } catch (e: Exception) {
            Log.i("DOLOGIN ERROR", "$e")
            listOf()
        }
        return response.map { it.toDomain() }
    }
}
