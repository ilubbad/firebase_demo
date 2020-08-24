package com.app.contactapp.network.Rest

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
object ApiClient {

    private const val SERVER_URL = "https://us-central1-contactapp-6dc9c.cloudfunctions.net/webApi/"
    private const val API_VERSION = "api/v1/"
    private const val BASE_URL = SERVER_URL + API_VERSION
    fun getApiClient(context: Context): ApiInterface {
        val loggingInterceptor = LoggingInterceptor()
        val cacheFile = File(context.cacheDir, "http_cache")
        cacheFile.mkdir()
        val cache = Cache(cacheFile, 10 * 1000 * 1000)
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .cache(cache)
            .build()


        val gson1 = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())

            .addConverterFactory(GsonConverterFactory.create(gson1))

            .client(okHttpClient)
            .build()
        return retrofit.create(ApiInterface::class.java)
    }

    private class LoggingInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()

            /**
             * Add Headers to Request
             */
            val t1 = System.nanoTime()
            val response = chain.proceed(request)
            val t2 = System.nanoTime()
            val responseLog = String.format(
                Locale.US, "Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6, response.headers()
            )
            val bodyString = response.body()!!.string()
            Log.d("TAG", "response\n$responseLog\n Response Body : $bodyString")
            return response.newBuilder()
                .body(ResponseBody.create(response.body()!!.contentType(), bodyString))
                .build()
        }
    }
}