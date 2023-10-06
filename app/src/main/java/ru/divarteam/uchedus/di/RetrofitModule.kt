package ru.divarteam.uchedus.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.divarteam.uchedus.network.RestAPI
import ru.divarteam.uchedus.network.RetrofitClient
import ru.divarteam.uchedus.network.RetrofitService

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    companion object {
        val BASE_URL = "https://api.hackathon.divarteam.ru/v1/"
    }

    @Provides
    @Reusable
    fun provideRetrofitService(): RetrofitService =
        RetrofitService(RetrofitClient.getClient(BASE_URL).create(RestAPI::class.java))
}