package com.fineweather.android.logic.network

import android.util.Log
import com.fineweather.android.logic.dao.LogUtil
import retrofit2.await

object FineWeatherNetwork {
    private val placeService=ServiceCreator.create<PlaceService>()
    suspend fun searchPlaces(query:String)=placeService.searchPlaces(query).await()
}