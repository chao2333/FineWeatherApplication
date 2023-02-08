package com.fineweather.android.ui.location

import android.app.Application
import androidx.lifecycle.ViewModel
import com.fineweather.android.logic.Respository

class LocationViewModel:ViewModel() {
    val database=Respository.getLocationDao()
}