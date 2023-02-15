package com.fineweather.android.logic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationEntity(var AccurateLocation:String, var RoughLocation:String, var lat:String,var lng:String,var sourcetype:Int ){
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0
}
