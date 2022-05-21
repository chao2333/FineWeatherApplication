package com.fineweather.android.ui.place

import android.view.animation.Transformation
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fineweather.android.logic.Respository
import com.fineweather.android.logic.model.Place

class PlaceViewModel:ViewModel() {
    private val searchLiveData=MutableLiveData<String>()
    //对城市数据缓存
    val placelise=ArrayList<Place>()
    //使用transformation.switchmap观察对象
    val placeLiveData= Transformations.switchMap(searchLiveData){ query->
        Respository.searchPlaces(query)
    }
    fun searchPlaces(query:String){
        searchLiveData.value=query
    }

}