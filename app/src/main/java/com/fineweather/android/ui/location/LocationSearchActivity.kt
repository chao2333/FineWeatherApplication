package com.fineweather.android.ui.location

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.FineWeatherApplication.Companion.context
import com.fineweather.android.R
import com.fineweather.android.logic.Respository
import com.fineweather.android.logic.dao.LogUtil
import com.fineweather.android.logic.dao.SaveLocationDatabase
import com.fineweather.android.logic.model.LocationEntity
import com.fineweather.android.ui.PlaceAdapter
import com.fineweather.android.ui.place.PlaceViewModel
import kotlinx.android.synthetic.main.activity_location_search.*
import kotlin.concurrent.thread


class LocationSearchActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter:PlaceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_search)
        //获取是否为开始界面进入，1为开始界面进入
        val flag=intent.getIntExtra("firstload",0)
        LogUtil.d("locationsearchtest",flag.toString())
        //不在activity生命周期中进行获取焦点和弹出软键盘
        val imm:InputMethodManager= this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val size5=LocationSearchCHENGDU.textSize
        LogUtil.d("searcheditable",size5.toString())
        searchPlaceEdit.textSize=14.0f
        searchPlaceEdit.postDelayed(Runnable() {
            searchPlaceEdit.requestFocus()
            val imm:InputMethodManager= this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS )
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        },500)
        //Recycle的赋值，设置livedata观察对象 更改activity->this
        val layoutManager=LinearLayoutManager(this)
        LocationSearchRecyclerview.layoutManager=layoutManager
        adapter= PlaceAdapter(this,viewModel.placelise,intent.getIntExtra("firstload",0),viewModel.database)
        LocationSearchRecyclerview.adapter=adapter
        ViewCompat.setTransitionName(searchPlaceEdit, "SearchTransitionAnimation")
        LocationSearchCancle.setOnClickListener {
            onBackPressed()
        }
        searchPlaceEdit.addTextChangedListener{ editable->
            val content=editable.toString()
            LogUtil.d("searcheditable",content)
            if(content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }else{
                LocationSearchRecyclerview.visibility=GONE
                LocationSearchhotcityLyout.visibility=View.VISIBLE
                viewModel.placelise.clear()
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.placeLiveData.observe(this, Observer{ result ->
            val places = result.getOrNull()
            if (places != null) {
                LocationSearchRecyclerview.visibility = View.VISIBLE
                LocationSearchhotcityLyout.visibility = GONE
                viewModel.placelise.clear()
                viewModel.placelise.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        //为每个按钮设置点击事件
        LocationSearchNOW.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)

            }else{
                val location=getLastKnownLocation()
                if (location==null){

                    val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 8f, object :
                        LocationListener {
                        override fun onLocationChanged(p0: Location) {

                            val nowlng=p0.longitude.toString()
                            val nowlat=p0.latitude.toString()
                            val pers= Respository.getSqlite()
                            if (intent.getIntExtra("firstload",0)==1&&pers!=null){
                                val edit=pers.edit()
                                edit.putString("lng",nowlng)
                                edit.putString("lat",nowlat)
                                edit.putString("name","当前位置")
                                edit.putInt("sourcetype",1)
                                edit.apply()
                            }
                            thread{
                                viewModel.database.insert(LocationEntity("当前位置","当前位置", nowlat,nowlng,1))
                            }
                            Toast.makeText(FineWeatherApplication.context,"已经添加当前位置到我的城市",Toast.LENGTH_LONG).show()
                            /*
                            val dbHelper= SaveLocationDatabase(context,"LocationSave.db",1)
                            val db=dbHelper.writableDatabase
                            val insert1= ContentValues().apply {
                                put("AccurateLocation","当前位置")
                                put("RoughLocation","当前位置")
                                put("lat",nowlat)
                                put("lng",nowlng)
                                put("sourcetype",1)
                            }
                            db.insert("Location",null,insert1)

                            dbHelper.close()
                             */
                            locationManager.removeUpdates(this)
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
                    })
                }else{
                    val lat=location.latitude.toString()
                    val lng=location.longitude.toString()
                    val pers= Respository.getSqlite()
                    if (intent.getIntExtra("firstload",0)==1&&pers!=null){
                        val edit=pers.edit()
                        edit.putString("lng",lng)
                        edit.putString("lat",lat)
                        edit.putString("name","当前位置")
                        edit.putInt("sourcetype",1)
                        edit.apply()
                    }
                    thread{
                        viewModel.database.insert(LocationEntity("当前位置","当前位置", lat,lng,1))
                    }
                    Toast.makeText(FineWeatherApplication.context,"已经添加当前位置到我的城市",Toast.LENGTH_LONG).show()
                    /*
                    val dbHelper= SaveLocationDatabase(this,"LocationSave.db",1)
                    val db=dbHelper.writableDatabase
                    val insert1= ContentValues().apply {
                        put("AccurateLocation","当前位置")
                        put("RoughLocation","当前位置")
                        put("lat",lat)
                        put("lng",lng)
                        put("sourcetype",1)
                    }
                    db.insert("Location",null,insert1)

                    dbHelper.close()
                     */
                    this.onBackPressed()
                }
            }
        }
        LocationSearchCHONGQING.setOnClickListener { searchPlaceEdit.setText("重庆市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)}
        LocationSearchSHANGHAISHI.setOnClickListener { searchPlaceEdit.setText("上海市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) }
        LocationSearchBEIJING.setOnClickListener { searchPlaceEdit.setText("北京市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) }
        LocationSearchCHENGDU.setOnClickListener { searchPlaceEdit.setText("成都市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) }
        LocationSearchGUANGZHOU.setOnClickListener { searchPlaceEdit.setText("广州市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) }
        LocationSearchSHENZHEN.setOnClickListener { searchPlaceEdit.setText("深圳市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) }
        LocationSearchTIANJIN.setOnClickListener { searchPlaceEdit.setText("天津市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) }
        LocationSearchWUHAN.setOnClickListener { searchPlaceEdit.setText("武汉市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) }
        LocationSearchXIAN.setOnClickListener { searchPlaceEdit.setText("西安市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) }
        LocationSearchSUZHOU.setOnClickListener { searchPlaceEdit.setText("苏州市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) }
        LocationSearchZHENGZHOU.setOnClickListener { searchPlaceEdit.setText("郑州市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) }
        LocationSearchHANGHZOU.setOnClickListener { searchPlaceEdit.setText("杭州市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) }
        LocationSearchSHIJIAZHUANG.setOnClickListener { searchPlaceEdit.setText("石家庄市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) }
        LocationSearchQINGDAO.setOnClickListener { searchPlaceEdit.setText("青岛市")
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) }
    }
    private fun getLastKnownLocation(): Location? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val providers = locationManager.allProviders
        var bestLocation: Location? = null
        for (provider in providers) {
            val l = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                bestLocation = l
            }
        }

        return bestLocation
    }

}

