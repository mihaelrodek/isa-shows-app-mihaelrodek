package com.android.infinum.retrofit

import android.content.SharedPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitClient {


    private const val BASE_URL = "https://tv-shows.infinum.academy"

    private val ACCESS_TOKEN = "acces-token"
    private var CLIENT = "client"
    private var UID = "uid"


    lateinit var retrofit: ApiService

    fun initRetrofit(preferences: SharedPreferences) {

        val okhttp = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(Interceptor { chain ->
                val builder = chain.request().newBuilder()

                val accessToken = preferences.getString(ACCESS_TOKEN, null)
                val clientToken = preferences.getString(CLIENT, null)
                val uid = preferences.getString(UID, null)
                if (accessToken != null) {
                    builder.header(ACCESS_TOKEN, accessToken)
                }
                if (clientToken != null) {
                    builder.header(CLIENT, clientToken)
                }
                if (uid != null) {
                    builder.header(UID, uid)
                }
                return@Interceptor chain.proceed(builder.build())
            })
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaType()))
            .client(okhttp)
            .build()
            .create(ApiService::class.java)

    }

}