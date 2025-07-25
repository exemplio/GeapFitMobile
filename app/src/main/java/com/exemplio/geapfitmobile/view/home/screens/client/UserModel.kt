package com.exemplio.geapfitmobile.view.home.screens.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("id_token") val idToken: String? = null,
    @SerialName("expires_in") val expiresIn: Int? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("token_type") val tokenType: String? = null,
    @SerialName("first") val first: String? = null,
    @SerialName("last") val last: String? = null,
)
