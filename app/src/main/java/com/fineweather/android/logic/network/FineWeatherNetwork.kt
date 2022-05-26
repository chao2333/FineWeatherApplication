package com.fineweather.android.logic.network

import android.util.Log
import com.fineweather.android.logic.dao.LogUtil
import retrofit2.await

object FineWeatherNetwork {
    //getlocation
    private val placeService=ServiceCreator.create<PlaceService>()
    suspend fun searchPlaces(query:String)=placeService.searchPlaces(query).await()
    //getWeather
    private val WeatherService=ServiceCreator.create<WeatherService>()
     suspend fun requestWeather(location:String)= WeatherService.requestWeather(location).await()
}

suspend fun main(){
    val weatherResponse1=FineWeatherNetwork.requestWeather("129.56316,46.327773")
    return
}