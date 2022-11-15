package com.fineweather.android.logic.network

import com.fineweather.android.logic.Respository
import com.fineweather.android.logic.dao.LogUtil
import retrofit2.await

object FineWeatherNetwork {
    //getlocation
    private val placeService=ServiceCreator.create<PlaceService>()
    suspend fun searchPlaces(query:String)=placeService.searchPlaces(query).await()
    //getWeather
    private val WeatherService=ServiceCreator.create<WeatherService>()
     suspend fun requestWeather(location:String)= WeatherService.requestWeather(location).await()
    //ConnectServer
    private val ConnectServer=ConnectServerCreator.create<ConnectSeverService>()
    suspend fun ConnectServer(lat: String, lng:String, address:String, id:Int)= ConnectServer.requestaccess(lat,lng,address,id).await()
}

