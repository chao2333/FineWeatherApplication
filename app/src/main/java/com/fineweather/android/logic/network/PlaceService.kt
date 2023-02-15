package com.fineweather.android.logic.network

import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.logic.model.CoordinateResponse
import com.fineweather.android.logic.model.PlaceResponse
import com.fineweather.android.logic.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/place?token=${FineWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}
interface   WeatherService{
    @GET("v2.6/${FineWeatherApplication.TOKEN}/{location}/weather?alert=true&realtime&dailysteps=15&hourlysteps=24")
    fun requestWeather(@Path("location") location:String): Call<WeatherResponse>
}
interface ConnectSeverService{
    @GET("data")
    fun requestaccess(@Query("lat") lat:String, @Query("lng") lng:String, @Query("address") address:String, @Query("id") id:Int):Call<respone>
}
interface CoordinateService {
    @GET("v3/")
    fun getCoordinateReverse(@Query("ak") ak: String =FineWeatherApplication.AK, @Query("output") output:String="json", @Query("coordtype") coordtype:String="wgs84ll", @Query("location") location:String):Call<CoordinateResponse>
}