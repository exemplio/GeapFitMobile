package com.exemplio.geapfitmobile.domain.repository

import com.exemplio.geapfitmobile.data.datasource.api.ApiServices
import com.exemplio.geapfitmobile.data.repository.ClientRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 1. Build your Retrofit instance
val retrofit = Retrofit.Builder()
    .baseUrl("https://firestore.googleapis.com/") // <-- use your base URL here
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// 2. Create your API service
val clientApiService = retrofit.create(ApiServices::class.java)

// 3. Pass it to your repository/viewmodel
val clientRepository = ClientRepositoryImpl(clientApiService)
