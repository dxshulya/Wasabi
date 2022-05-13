package com.dxshulya.wasabi.core.di

import com.dxshulya.wasabi.data.Api
import com.dxshulya.wasabi.data.datastore.SharedPreference
import com.dxshulya.wasabi.core.token.TokenInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule(private val sharedPreference: SharedPreference) {

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
            addInterceptor(TokenInterceptor(sharedPreference))
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
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAPI(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}