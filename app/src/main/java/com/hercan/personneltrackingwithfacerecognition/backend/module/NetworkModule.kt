package com.hercan.personneltrackingwithfacerecognition.backend.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hercan.personneltrackingwithfacerecognition.backend.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://172.20.32.141:5000/"

    @Singleton
    @Provides
    fun providesRetrofit(
        client: OkHttpClient, gson: Gson
    ): Retrofit {
        return Retrofit.Builder().client(client).baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    @Singleton
    @Provides
    fun providesApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun providesGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun providesOkHttp(): OkHttpClient = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build()
}