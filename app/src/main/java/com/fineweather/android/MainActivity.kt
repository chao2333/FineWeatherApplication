package com.fineweather.android

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fineweather.android.logic.dao.LogUtil
import com.fineweather.android.ui.WelcomeActivity
import com.fineweather.android.ui.location.LocationActivity
import com.fineweather.android.ui.place.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.main_threedayweatheritem.view.*
import androidx.appcompat.R.anim.abc_fade_in as animAbc_fade_in

class MainActivity : AppCompatActivity() {
    private val model by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val test=model.getcurrent.value
        upDataTextView.text=test.toString()
        model.getcurrent.observe(this, Observer { test1:Int->
            upDataTextView.text=test1.toString()
        })
       topTextView.text=""
        //实现欢迎界面
        Welcome()
        topSettingButton.setOnClickListener{
            coordinatorlayout.setBackgroundResource(R.drawable.mainrainy)
            val LocationIntent3= Intent(this, WelcomeActivity::class.java)
            startActivity(LocationIntent3)
        }
        topLocationButton.setOnClickListener{
            coordinatorlayout.setBackgroundResource(R.drawable.mainsunny)
            val LocationIntent= Intent(this, LocationActivity::class.java)
            startActivity(LocationIntent)
        }
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
    }
    fun Welcome(){
        val ApplicationDataPers=getSharedPreferences("ApplicationData",0)
        val editor=ApplicationDataPers.edit()
        val Firstload=ApplicationDataPers.getBoolean("Firstload",false)
        editor.apply()
        if(!Firstload){
            val WelcomeIntent=Intent(this,WelcomeActivity::class.java)
            startActivity(WelcomeIntent)
        }
        editor.putBoolean("Firstload",true)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        val pers=FineWeatherApplication.context.getSharedPreferences("ApplicationData",0)
        val accu=pers.getString("address","")
        val roug=pers.getString("name","")
        topTextView.text=roug
    }
}
