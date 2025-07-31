package com.example.geapfit.models

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val roles: List<String>? = null
)