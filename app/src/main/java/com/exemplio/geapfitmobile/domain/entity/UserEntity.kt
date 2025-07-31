package com.exemplio.geapfitmobile.domain.entity


data class UserEntity(
    val userId: String,
    val name: String,
    val nickname: String,
    val followers: Int,
    val following: List<String>,
    val userMode: UserMode,
    val verified:Boolean,
    val first: String? = null,
    val last: String? = null,
)

sealed class UserMode(val userType: Int) {
    data object REGULAR_USER : UserMode(0)
    data object CONTENT_CREATOR_USER : UserMode(1)
    data object COMPANY_USER : UserMode(2)
}