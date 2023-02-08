package com.fineweather.android.ui.place


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fineweather.android.logic.Respository


class MainViewModel:ViewModel() {
    private val locationLiveData=MutableLiveData<String>()

    val weatherLiveData=Transformations.switchMap(locationLiveData){
        Respository.refreshWeather(it)

    }

    fun refreshWeather(location:String){
        locationLiveData.value=location
    }
    fun getSharepreferences()=Respository.getSqlite()
    fun getLocationDao()=Respository.getLocationDao()
}