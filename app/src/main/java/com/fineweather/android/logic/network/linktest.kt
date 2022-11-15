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
interface   WeatherService1{
    @GET("data?lat=111111&lng=222222&address=中国河南省%20郑州市%20新郑市&id=2662123")
    fun requestWeather(): Call<respone>
}
private val WeatherService2=ServiceCreator3.create<WeatherService1>()
suspend fun requestWeather3()= WeatherService2.requestWeather().await()
suspend fun main(){
    val er= requestWeather3()
    print(er.status)
}

data class respone(val status:String)
fun hanve(){
    var connection:HttpURLConnection?=null
    val url = URL("http://fineweatherapp.cooc.site:8080/test.jsp?date=Data&mac=Mac")
    for (i in 1..100000){
        connection=url.openConnection() as HttpURLConnection
        connection.inputStream
        print("have save")
    }
}