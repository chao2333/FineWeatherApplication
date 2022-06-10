package com.fineweather.android.logic

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.logic.model.Place
import com.fineweather.android.logic.model.WeatherResponse
import com.fineweather.android.logic.network.FineWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

object Respository {
    fun searchPlaces(query:String)=liveData(Dispatchers.IO){
        val result=try {
            val placeResponse=FineWeatherNetwork.searchPlaces(query)
            if(placeResponse.status=="ok"){
                val places=placeResponse.places
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response is ${placeResponse.status}"))
            }
        }catch(e:Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }
    fun refreshWeather(Location:String)=liveData(Dispatchers.IO){
        val result=try {

            val realtimeResponse= FineWeatherNetwork.requestWeather(Location)
            if (realtimeResponse.status=="ok"){
                Result.success(realtimeResponse)
            }else{
                Result.failure(
                    java.lang.RuntimeException(
                        "realtime response status is ${realtimeResponse.status}"
                    )
                )
            }

        }catch (e:Exception){
            Result.failure<WeatherResponse>(e)
        }
        emit(result)
    }
    fun getSqlite()=FineWeatherApplication.context.getSharedPreferences("ApplicationData",0)

}