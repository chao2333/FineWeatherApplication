package com.fineweather.android.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
    private val a=MutableLiveData<Int>()
    val getcurrent:LiveData<Int>
            get() = a
    init {
        a.value=3
    }
    fun rese(){
        a.value=a.value!!+1
    }
}