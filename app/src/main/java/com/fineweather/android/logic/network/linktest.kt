package com.fineweather.android.logic.network

import android.app.Person
import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.logic.model.WeatherResponse
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.DataOutputStream
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import kotlin.concurrent.thread
import kotlin.math.max
object ServiceCreator3 {
    private const val BASE_URL="http://fineweatherapp.cooc.site/"
    private val retrofit= Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}
data class respone(val status:String)
