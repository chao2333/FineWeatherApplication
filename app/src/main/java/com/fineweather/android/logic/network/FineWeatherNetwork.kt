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
    suspend fun coordinateService(location:String)= CoordinateService.getCoordinateReverse(location = location).await()
}

suspend fun main(){
    //val text="{\"status\":0,\"result\":{\"location\":{\"lng\":113.98569608165529,\"lat\":33.57463260586307},\"formatted_address\":\"河南省漯河市源汇区宝塔山路\",\"business\":\"\",\"addressComponent\":{\"country\":\"中国\",\"country_code\":0,\"country_code_iso\":\"CHN\",\"country_code_iso2\":\"CN\",\"province\":\"河南省\",\"city\":\"漯河市\",\"city_level\":2,\"district\":\"源汇区\",\"town\":\"\",\"town_code\":\"\",\"distance\":\"\",\"direction\":\"\",\"adcode\":\"411102\",\"street\":\"宝塔山路\",\"street_number\":\"\"},\"pois\":[],\"roads\":[],\"poiRegions\":[],\"sematic_description\":\"\",\"cityCode\":344}}"

    //val b=CoordinateService

    return
}