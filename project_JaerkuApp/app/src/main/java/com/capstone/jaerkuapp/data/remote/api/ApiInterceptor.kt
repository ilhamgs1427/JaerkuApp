package com.capstone.jaerkuapp.data.remote.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiInterceptor(private var token: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()

        request = if (request.header("No-Authentication") == null && token.isNotEmpty()) {
            val finalToken = "$token"
            request.newBuilder()
                .addHeader("Authorization", finalToken)
                .build()
        } else {
            request.newBuilder()
                .build()
        }

        return chain.proceed(request)
    }
}