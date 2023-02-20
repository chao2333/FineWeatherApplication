package com.fineweather.android.ui

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration


object CustomDensityUtil {
    private var previousDensity = 0f
    private var previousScaledDensity = 0f
    fun setCustomDensity(activity: Activity, application: Application) {
        val appDisplayMetrics = application.resources.displayMetrics
        if (previousDensity == 0f) {
            previousDensity = appDisplayMetrics.density
            previousScaledDensity = appDisplayMetrics.scaledDensity
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(p0: Configuration) {
                    if (p0.fontScale > 0) {
                        previousScaledDensity = application.resources.displayMetrics.scaledDensity
                    }
                }
                override fun onLowMemory() {}
            })
        }
        val targetDensity = (appDisplayMetrics.widthPixels / 380.0).toFloat()
        val targetScaleDensity = targetDensity * (previousScaledDensity / previousDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()
        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaleDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }
}