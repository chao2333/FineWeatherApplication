package com.fineweather.android.ui

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.R
import com.fineweather.android.logic.Respository
import com.fineweather.android.logic.dao.LogUtil
import com.fineweather.android.logic.dao.SaveLocationDatabase
import com.fineweather.android.ui.location.LocationSearchActivity
import com.fineweather.android.ui.setting.SettingPrivacypolicyActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    val pers= Respository.getSqlite()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        setContentView(R.layout.activity_welcome)
        WelcomeButton.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),11)
            }
            val edit2=pers.edit()
            edit2.putBoolean("Firstload",true)
            edit2.putInt("sourcetype",1)
            edit2.apply()

        }
        RefuseButton.setOnClickListener {
            val edit=pers.edit()
            edit.putBoolean("Firstload",true)
            edit.apply()
            Toast.makeText(this, "请手动添加地点", Toast.LENGTH_SHORT).show()
            val intent=Intent(this,LocationSearchActivity::class.java)
            intent.putExtra("firstload",1)
            startActivity(intent)
            finish()
        }
        welcome_4.setOnClickListener {
            val intent21=Intent(this,SettingPrivacypolicyActivity::class.java)
            startActivity(intent21)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            11->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    var nowlng=""
                    var nowlat=""
                        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
                            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                                    val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                                    if (location!=null){
                                        nowlng=location.longitude.toString()
                                        nowlat=location.latitude.toString()
                                        val edit23=pers.edit()
                                        edit23.putString("lat",nowlat)
                                        edit23.putString("lng",nowlng)
                                        edit23.putString("name","当前定位")
                                        edit23.apply()
                                    }
                                    if(location==null){
                                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 8f, object :
                                            LocationListener {
                                            override fun onLocationChanged(p0: Location) {
                                                // 当GPS定位信息发生改变时，更新位置
                                                //   LogUtil.d("nowLocationtest","速度"+p0.speed.toString()+",方向"+p0.bearing.toString()+",经度"+p0.longitude.toString()+",纬度"+p0.latitude.toString())
                                                nowlng=p0.longitude.toString()
                                                nowlat=p0.latitude.toString()
                                                val edit2=pers.edit()
                                                edit2.putString("lat",nowlat)
                                                edit2.putString("lng",nowlng)
                                                edit2.putString("name","当前定位")
                                                edit2.apply()
                                                locationManager.removeUpdates(this)
                                            }
                                            override fun onProviderDisabled(provider: String) {
                                            }
                                            override fun onProviderEnabled(provider: String) {
                                            }
                                            override fun onStatusChanged(
                                                provider: String, status: Int,
                                                extras: Bundle
                                            ) {
                                            }
                                        })
                                    }
                                }
                            }
                } else {
                    Toast.makeText(this, "你拒绝了授权位置权限，请手动添加地点", Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,LocationSearchActivity::class.java)
                    intent.putExtra("firstload",1)
                    startActivity(intent)
                }
                finish()
            }
        }
    }
}