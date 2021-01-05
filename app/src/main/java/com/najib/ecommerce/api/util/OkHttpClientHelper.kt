package com.najib.ecommerce.api.util

import com.najib.ecommerce.App
import com.najib.ecommerce.BuildConfig
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class OkHttpClientHelper {

    fun initOkHttpClient(): OkHttpClient {

        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        // Add logging for debug builds
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(logging)

            // Add Chuck interceptor
            okHttpClientBuilder.addInterceptor(ChuckInterceptor(App.context))
        }

        // Set timeout duration
        okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS)

        return okHttpClientBuilder.build()
    }
}