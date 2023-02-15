package com.fineweather.android.logic.model

import com.google.gson.annotations.SerializedName

data class CoordinateResponse(val status:String,val result:Result2){
    data class Result2(val formatted_address:String,val addressComponent:AddressComponent)
    data class AddressComponent(val formatAddress:String,val country:String,val province:String,val city:String,val district:String,val town:String,val street:String)
}
