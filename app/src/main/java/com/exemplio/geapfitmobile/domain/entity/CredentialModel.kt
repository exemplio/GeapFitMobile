package com.exemplio.geapfitmobile.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class CredentialModel(
    @SerialName("access_token") val accessToken: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("id_token") val idToken: String? = null,
    @SerialName("token_type") val tokenType: String? = null,
    @SerialName("expires_in") val expiresIn: Int? = null
) {
    fun toJson(): String = Json.encodeToString(serializer(), this)

    companion object {
        fun fromJson(json: String): CredentialModel =
            Json.decodeFromString(serializer(), json)
    }
}

//data class CredentialModel(
//    @SerializedName("access_token") val accessToken: String?,
//    @SerializedName("refresh_token") val refreshToken: String?,
//    @SerializedName("id_token") val idToken: String?,
//    @SerializedName("token_type") val tokenType: String?,
//    @SerializedName("expires_in") val expiresIn: Int?
//) {
//    fun toJson(): String = Gson().toJson(this)
//
//    companion object {
//        fun fromJson(json: String): CredentialModel = Gson().fromJson(json, CredentialModel::class.java)
//    }
//}