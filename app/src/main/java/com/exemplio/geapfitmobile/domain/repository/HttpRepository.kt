package com.exemplio.geapfitmobile.domain.repository

import com.exemplio.geapfitmobile.domain.entity.UserEntity
import okhttp3.OkHttpClient
import okhttp3.Response

interface HttpRepository {
    suspend fun response(client: OkHttpClient): Response
}