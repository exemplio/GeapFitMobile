package com.exemplio.geapfitmobile.domain.repository

import com.example.geapfit.models.CredentialModel
import com.example.geapfit.models.PasswordGrantRequest
import com.example.geapfit.models.Users
import com.exemplio.geapfitmobile.data.response.UserResponse
import retrofit2.http.*
import okhttp3.RequestBody

interface ApiRepository {
    @GET("doLogin/.json")
    suspend fun doLogin(): List<UserResponse>

    // Password Grant
    @POST
    suspend fun passwordGrant(
        @Url url: String,
        @Body request: PasswordGrantRequest,
        @HeaderMap headers: Map<String, String> = mapOf("Content-Type" to "application/json")
    ): CredentialModel

    // Resend Sign
    @GET
    suspend fun resendSign(
        @Url url: String,
        @HeaderMap headers: Map<String, String>
    ): Any

    // Recovery Questions
    @GET
    suspend fun recoveryQuestions(
        @Url url: String,
        @HeaderMap headers: Map<String, String>
    ): Any

    // Close Session
    @PUT
    suspend fun closeSession(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody = RequestBody.create(null, ByteArray(0))
    ): Void?

    // Get Clients
    @GET
    suspend fun getClients(
        @Url url: String,
        @HeaderMap headers: Map<String, String>
    ): Users
}