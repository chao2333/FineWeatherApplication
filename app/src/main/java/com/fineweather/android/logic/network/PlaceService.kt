package com.fineweather.android.logic.network

import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.logic.model.PlaceResponse
import com.fineweather.android.logic.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/place?token=${FineWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}
interface WeatherService{
    @GET("v2.6/${FineWeatherApplication.TOKEN}/{location}/weather?alert=true&realtime&dailysteps=15&hourlysteps=24")
    fun requestWeather(@Path("location") location:String): Call<WeatherResponse>
}
//https://api.caiyunapp.com/v2.6/qJq8YCdea1b5oTHw/109.374876,35  .64708/weather?alert=true&realtime&dailysteps=15&hourlysteps=24
//实时获取位置代码
/*val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
if (ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
) {
    // TODO: Consider calling
    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.
    return
}
locationManager.requestLocationUpdates(
    LocationManager.GPS_PROVIDER,
    2000, 8f, object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // 当GPS定位信息发生改变时，更新位置
            LogUtil.d("nowLocationtest","速度"+location.speed.toString()+",方向"+location.bearing.toString()+",经度"+location.longitude.toString()+",纬度"+location.latitude.toString())
        }
        override fun onProviderDisabled(provider: String) {
        }
        override fun onProviderEnabled(provider: String) {
            // 当GPS LocationProvider可用时，更新位置
        }
        override fun onStatusChanged(
            provider: String, status: Int,
            extras: Bundle
        ) {
        }
    })*/
fun main(){
    val string="刚察县气象台6月8日13时08分发布刚察县冰雹橙色预警信号：预计8日13时08分至8日19时08分刚察县将出现冰雹天气，并可能造成雹灾。请有关单位和人员做好防范准备。"
    val test=string.split("：")[1]
    println(test)
}