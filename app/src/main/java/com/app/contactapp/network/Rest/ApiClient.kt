package com.app.contactapp.network.Rest

import android.app.Application
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by ibraheem lubbad on 8/23/20.
 * Copyright (c) 2020 ContactApp. All rights reserved.
 */
object ApiClient {


    var SERVER_URL = "https://us-central1-contactapp-6dc9c.cloudfunctions.net/webApi/"
    private const val API_VERSION = "api/v1/"


    fun getApiClient(app: Application, serverUrl: String = SERVER_URL): ApiInterface {
        val loggingInterceptor = LoggingInterceptor()
        var BASE_URL = serverUrl + API_VERSION


        /*
        val cacheFile = File(app.applicationContext.cacheDir, "http_cache")
        cacheFile.mkdir()
        val cache = Cache(cacheFile, 10 * 1000 * 1000)*/
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            // .cache(cache)
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
            return response.newBuilder()
                .body(ResponseBody.create(response.body()!!.contentType(), bodyString))
                .build()
        }
    }
}