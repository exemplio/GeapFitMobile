package com.exemplio.geapfitmobile.data.di

import com.exemplio.geapfitmobile.data.service.HttpServiceImpl
import com.exemplio.geapfitmobile.data.datasource.api.ApiServices2
import com.exemplio.geapfitmobile.data.repository.AuthRepositoryImpl
import com.exemplio.geapfitmobile.data.service.IsOnlineProvider
import com.exemplio.geapfitmobile.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton
import android.content.Context
import com.exemplio.geapfitmobile.domain.repository.HttpRepository
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    @Singleton
    fun provideHttpService(client: OkHttpClient): HttpRepository {
        return HttpServiceImpl(client)
    }

    @Provides
    fun provideAuthRepository(
        api: ApiServices2,
        httpService: OkHttpClient,
        context: Context
    ): AuthRepository {
        return AuthRepositoryImpl(api, HttpServiceImpl(httpService), IsOnlineProvider(context))
    }

//    @Provides
//    fun provideClientRepository(api: ApiServices2): ClientRepository {
//        return ClientRepositoryImpl(api)
//    }

    @Provides
    fun provideApiServices(retrofit: Retrofit): ApiServices2 {
        return retrofit.create(ApiServices2::class.java)
    }

    @Provides
    fun provideRetrofit(json:Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://aristidevs-bd31d-default-rtdb.europe-west1.firebasedatabase.app/")
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()
    }

    @Provides
    fun provideJson():Json{
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
}

