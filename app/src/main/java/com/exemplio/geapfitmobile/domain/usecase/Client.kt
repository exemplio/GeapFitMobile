package com.exemplio.geapfitmobile.domain.usecase

import com.exemplio.geapfitmobile.domain.entity.UserEntity
import com.exemplio.geapfitmobile.domain.repository.AuthRepository
import javax.inject.Inject

class Client @Inject constructor(private val clientRepository: AuthRepository) {
    suspend operator fun invoke(user:String, password:String): UserEntity?{
        if(user.contains("@hotmail.com")){
            return null
        }
        val response = clientRepository.doLogin(user, password)
        return response.random()
    }
}