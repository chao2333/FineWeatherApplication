package com.fineweather.android.logic.network

import android.app.Service
import com.fineweather.android.logic.model.Place
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

object ServiceCreator {
    private const val BASE_URL="https://api.caiyunapp.com/"
    private val retrofit=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}
object ConnectServerCreator {
    private const val BASE_URL="https://fineweatherapp.cooc.site/"
    private val retrofit= Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}
object CoordinateCreator{
    private const val BASE_URL="https://api.map.baidu.com/reverse_geocoding/"
    private val retrofit=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass: Class<T>): T =retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}

