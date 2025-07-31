package com.example.geapfit.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("users") val users: List<UserModel>
) {
    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(json: String): Users = Gson().fromJson(json, Users::class.java)
    }
}