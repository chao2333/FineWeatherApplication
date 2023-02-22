package com.fineweather.android.logic.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//Location
data class PlaceResponse(val status:String,val query:String,val places:List<Place>)
data class Place(val name: String, val location: Location,@SerializedName("formatted_address") val address: String)
data class Location(val lat:String,val lng:String)
data class LocationSaveItem(val name: String,val address: String,val lat:String,val lng:String)
data class LocationSaveItem2(val name: String,val address: String,val lat:String,val lng:String,val sourceType:Int)
//Weather
data class WeatherResponse(val status: String,val result:Result)
data class Result(val alert:Alert,val realtime:Realtime,val minutely:Minutely,val hourly:Hourly,val daily:Daily,@SerializedName("forecast_keypoint") val summary:String)
    data class Alert(val status: String,val content:ArrayList<AlertDetail>):Serializable
        data class AlertDetail(val status: String,val code:String,val description:String,val title:String,val source:String,val location:String):Serializable
    data class Realtime(val status: String,val temperature:Double,val humidity:Double,val cloudrate:Double,val skycon:String,val wind:Wind,val pressure:Double,val apparent_temperature:Double,val air_quality:AirQuality,val life_index:LifeIndex)
        data class Wind(val speed:Double,val direction:Double):Serializable
        data class AirQuality(val pm25:Double,val pm10:Double,val o3:Double,val so2:Double,val no2:Double,val co:Double,val aqi:AqiR,val description:Description)
        data class Description(val chn:String)
        data class AqiR(val chn: Double)
        data class LifeIndex(val ultraviolet:Ultraviolet)
            data class Ultraviolet(val index:Double,val desc:String)
    data class Minutely(val status: String,val precipitation_2h:ArrayList<Double>,val precipitation:ArrayList<Double>)
    data class Hourly(val status: String,val precipitation:ArrayList<CommonUse>,val temperature:ArrayList<CommonUse>,val apparent_temperature:ArrayList<CommonUse>,val wind:ArrayList<WindH>,val humidity:ArrayList<CommonUse>,val cloudrate:ArrayList<CommonUse>,val skycon:ArrayList<Skyconh>,val pressure:ArrayList<CommonUse>,val visibility:ArrayList<CommonUse>,val air_quality:AirQualityH)
        data class CommonUse(val datetime:String,val value:Double)
        data class WindH(val datetime: String,val speed: Double,val direction:Double)
        data class Skyconh(val datetime: String,val value:String)
        data class AirQualityH(val aqi: ArrayList<Aqi>,val pm25: ArrayList<CommonUse>)
            data class Aqi(val datetime: String,val value:ValueA)
                data class ValueA(val chn:Double,val usa:Double):Serializable
    data class Daily(val status: String,val astro:ArrayList<Astro>,val precipitation: ArrayList<CommonUse2>,val temperature:ArrayList<CommonUse2>,val wind: ArrayList<WindD>,val humidity: ArrayList<CommonUse2>,val cloudrate:ArrayList<CommonUse2>,val pressure:ArrayList<CommonUse2>,val visibility:ArrayList<CommonUse2>,val air_quality:AirQualityD,val skycon:ArrayList<Skycon>,val life_index:LifeIndexD):Serializable                                                                                                             //
        data class Astro(val date:String,val sunrise:Sunrands,val sunset:Sunrands):Serializable
            data class Sunrands(val time:String):Serializable
        data class CommonUse2(val date:String,val max:Double,val min:Double,val avg:Double):Serializable
        data class WindD(val date: String,val max:Wind,val min:Wind,val avg:Wind):Serializable
        data class AirQualityD(val aqi: ArrayList<AqiD>,val pm25:ArrayList<CommonUse2>):Serializable
            data class AqiD(val date: String,val max:ValueA,val avg:ValueA,val min:ValueA):Serializable
        data class Skycon(val date:String,val value: String):Serializable
        data class LifeIndexD(val ultraviolet:ArrayList<CommonUse3>,val carWashing:ArrayList<CommonUse3>,val dressing:ArrayList<CommonUse3>,val comfort:ArrayList<CommonUse3>,val coldRisk:ArrayList<CommonUse3>):Serializable
            data class CommonUse3(val date: String,val index:String,val desc: String):Serializable
//Airquality界面需要的数据
class Airqualitydata:Serializable{
    var time:String=""
    var airquality: Double=0.0
    var pm25: Double=0.0
    var pm10: Double=0.0
    var so2: Double=0.0
    var no2: Double=0.0
    var o3: Double=0.0
    var co: Double=0.0
    var timearraylist=arrayListOf("")
    var aqichnlist=arrayListOf(0.0)
}
fun main(){
    val test2:Pair<Int,Int>
   // val test= arrayListOf(test2)
  //  print(test.toString())
}