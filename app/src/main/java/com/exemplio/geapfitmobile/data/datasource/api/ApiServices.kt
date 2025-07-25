package com.exemplio.geapfitmobile.data.datasource.api

import com.exemplio.geapfitmobile.data.response.UserResponse
import retrofit2.http.GET

interface ApiServices {

    @GET("doLogin/.json")
    suspend fun doLogin():List<UserResponse>

}