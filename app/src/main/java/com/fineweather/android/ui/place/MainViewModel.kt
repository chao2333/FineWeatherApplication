package com.fineweather.android.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fineweather.android.logic.Respository
import com.fineweather.android.logic.model.Location
import com.fineweather.android.logic.model.PlaceResponse
import com.fineweather.android.logic.model.WeatherResponse

class MainViewModel:ViewModel() {
    private val locationLiveData=MutableLiveData<String>()

    val weatherLiveData=Transformations.switchMap(locationLiveData){
        Respository.refreshWeather(it)

    }

    fun refreshWeather(location:String){
        locationLiveData.value=location
    }
    fun getSharepreferences()=Respository.getSqlite()

}