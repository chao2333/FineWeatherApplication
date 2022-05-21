package com.fineweather.android.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.fineweather.android.logic.model.Place
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
}