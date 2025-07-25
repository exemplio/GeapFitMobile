package com.exemplio.geapfitmobile.domain.repository

import com.exemplio.geapfitmobile.data.response.UserResponse
import com.exemplio.geapfitmobile.domain.entity.UserEntity
import retrofit2.Response
import retrofit2.http.GET

interface ClientApiService {
    @GET("doLogin/.json")
    suspend fun doLogin():List<UserResponse>

    @GET("ruta/del/endpoint")
    suspend fun getClientes(): Response<List<UserEntity>>
}