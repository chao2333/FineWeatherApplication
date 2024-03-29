package com.fineweather.android.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.logic.dao.LocationDatabase
import com.fineweather.android.logic.model.CoordinateResponse
import com.fineweather.android.logic.model.Place
import com.fineweather.android.logic.model.WeatherResponse
import com.fineweather.android.logic.network.FineWeatherNetwork
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import retrofit2.await
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
            var severResponse=""
            try {
                severResponse=FineWeatherNetwork.connectServer(
                    Respository.getSqlite().getString("lat","")!!,
                    Respository.getSqlite().getString("lng","")!!,
                    Respository.getSqlite().getString("address","")!!,
                    Respository.getSqlite().getInt("id",0)!!).status
            }catch (e:Exception){
                print(e)
            }
            if (realtimeResponse.status=="ok"&&severResponse!="no"){
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
    fun getLocationDao()=LocationDatabase.getLocationDatabase().locationDao()
    fun refreshCoordinate(location:String)=liveData(Dispatchers.IO){
        val result=try {
            val coordinateResponse=FineWeatherNetwork.coordinateService(location)
            if (coordinateResponse.status=="0") {
                Result.success(coordinateResponse)
            }else{
                Result.failure(java.lang.RuntimeException("get coordinate fail"))
            }

        }catch (e:Exception){
            Result.failure<CoordinateResponse>(e)
        }
        emit(result)

    }
}
suspend fun main(){
    val t =FineWeatherNetwork.requestWeather("113.681442,34.591229")
}