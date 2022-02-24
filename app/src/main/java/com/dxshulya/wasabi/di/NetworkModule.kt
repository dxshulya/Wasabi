package com.dxshulya.wasabi.di

import com.dxshulya.wasabi.data.Api
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpClient.run {
            readTimeout((60*2).toLong(), TimeUnit.SECONDS)
            connectTimeout((60*2).toLong(), TimeUnit.SECONDS)
            writeTimeout((60*2).toLong(), TimeUnit.SECONDS)
            addInterceptor(interceptor)
        }
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://back-nest-js.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAPI(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}