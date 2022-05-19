package com.dxshulya.wasabi.core.token

import com.dxshulya.wasabi.domain.datastore.SharedPreference
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val sharedPreference: SharedPreference) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()

        val token = sharedPreference.token

        if(token.isNotBlank()) {
            builder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(builder.build())
    }
}