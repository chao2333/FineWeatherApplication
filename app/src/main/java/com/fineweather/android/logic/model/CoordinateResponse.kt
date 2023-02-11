package com.fineweather.android.logic.model

import com.google.gson.annotations.SerializedName

data class CoordinateResponse(val status:String,val addressComponent:AddressComponent){
    data class AddressComponent(val country:String,val province:String,val city:String,val district:String,val town:String,val street:String)
}
