package com.exemplio.geapfitmobile.data.di

import com.exemplio.geapfitmobile.data.service.HttpServiceImpl
import com.exemplio.geapfitmobile.data.repository.AuthRepositoryImpl
import com.exemplio.geapfitmobile.data.service.IsOnlineProvider
import com.exemplio.geapfitmobile.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton
import android.content.Context
import com.exemplio.geapfitmobile.data.service.ApiServicesImpl
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
    fun provideHttpService(client: OkHttpClient) : HttpServiceImpl {
        return HttpServiceImpl(client)
    }

    @Provides
    fun provideApiService(client: OkHttpClient, context: Context): ApiServicesImpl {
        return ApiServicesImpl( HttpServiceImpl(client), IsOnlineProvider(context))
    }

    @Provides
    fun provideAuthRepository(
        api: ApiServicesImpl,
        httpService: OkHttpClient,
        context: Context
    ): AuthRepository {
        return AuthRepositoryImpl(api, HttpServiceImpl(httpService), IsOnlineProvider(context))
    }
}

