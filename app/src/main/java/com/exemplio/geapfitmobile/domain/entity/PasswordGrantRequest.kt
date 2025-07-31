package com.example.geapfit.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

import kotlinx.serialization.Serializable


@Serializable
data class PasswordGrantRequest(
    val username: String,
    val password: String,
    val grantType: String = "password",
    val clientId: String,
    val clientSecret: String? = null,
    val scope: String? = null
)

//data class PasswordGrantRequest(
//    @SerializedName("username") val username: String,
//    @SerializedName("password") val password: String,
//    @SerializedName("grant_type") val grantType: String = "password",
//    @SerializedName("client_id") val clientId: String,
//    @SerializedName("client_secret") val clientSecret: String? = null,
//    @SerializedName("scope") val scope: String? = null
//) {
//    fun toJson(): String = Gson().toJson(this)
//
//    companion object {
//        fun fromJson(json: String): PasswordGrantRequest =
//            Gson().fromJson(json, PasswordGrantRequest::class.java)
//    }
//}