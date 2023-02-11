package com.fineweather.android.logic.network

import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.logic.model.CoordinateResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CoordinateService {
    @GET("v3/?ak=${FineWeatherApplication.AK}&output=json&coordtype=wgs84ll&location={lat},{lng}")
    fun getCoordinateReverse(@Path("lat") lat:String,@Path("lng") lng:String):Call<CoordinateResponse>
}