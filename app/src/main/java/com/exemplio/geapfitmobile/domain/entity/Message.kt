package com.example.geapfit.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message") val message: String?,
    @SerializedName("cause") val cause: List<String>? = null
) {
    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(json: String): Message = Gson().fromJson(json, Message::class.java)
    }
}