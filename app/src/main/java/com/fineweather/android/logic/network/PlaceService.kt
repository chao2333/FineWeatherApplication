package com.fineweather.android.logic.network

import com.fineweather.android.FineWeatherApplication
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
interface WeatherService{
    @GET("v2.6/${FineWeatherApplication.TOKEN}/{location}/weather?alert=true&realtime&dailysteps=15&hourlysteps=24")
    fun requestWeather(@Path("location") location:String): Call<WeatherResponse>
}
//https://api.caiyunapp.com/v2.6/qJq8YCdea1b5oTHw/109.374876,35.64708/weather?alert=true&realtime&dailysteps=15&hourlysteps=24