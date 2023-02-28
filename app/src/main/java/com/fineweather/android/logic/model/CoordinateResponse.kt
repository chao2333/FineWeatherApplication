package com.fineweather.android.logic.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CoordinateResponse(val status:String,val result:Result2): Serializable {
    data class Result2(val formatted_address:String,val addressComponent:AddressComponent):Serializable
    data class AddressComponent(val formatAddress:String,val country:String,val province:String,val city:String,val district:String,val town:String,val street:String):Serializable
}
