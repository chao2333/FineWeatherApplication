package com.fineweather.android.logic.network

import com.fineweather.android.logic.Respository
import com.fineweather.android.logic.dao.LogUtil
import com.fineweather.android.logic.model.CoordinateResponse
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
    suspend fun connectServer(lat: String, lng:String, address:String, id:Int)= ConnectServer.requestaccess(lat,lng,address,id).await()
    private val CoordinateService=CoordinateCreator.create<CoordinateService>()
    suspend fun coordinateService(lat:String,lng:String)= CoordinateService.getCoordinateReverse(lat,lng)
}

suspend fun main(){
    val text=FineWeatherNetwork.coordinateService("33.570094","113.973451")
    return
}