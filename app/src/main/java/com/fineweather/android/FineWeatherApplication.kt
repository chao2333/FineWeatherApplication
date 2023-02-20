package com.fineweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics

class FineWeatherApplication:Application() {
    companion object {
        const val AK="uTnRGBeog3pBSd9paHnn0EQFHU4PFqCl"
        const val TOKEN="qJq8YCdea1b5oTHw"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}