package com.exemplio.geapfitmobile.data.service

import com.exemplio.geapfitmobile.domain.repository.HttpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Response
import javax.inject.Inject

data class UserEntity(val username: String, val email: String)

class HttpServiceImpl @Inject constructor(
    private val client: OkHttpClient
) : HttpRepository {
    suspend fun response(
        f: suspend (OkHttpClient) -> Response
    ): Response = withContext(Dispatchers.IO) {
        f(client)
    }

    override suspend fun response(client: OkHttpClient): Response {
        TODO("Not yet implemented")
    }

}