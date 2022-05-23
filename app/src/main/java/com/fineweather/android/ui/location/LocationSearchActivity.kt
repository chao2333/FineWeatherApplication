package com.fineweather.android.ui.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fineweather.android.R
import com.fineweather.android.logic.dao.LogUtil
import com.fineweather.android.ui.PlaceAdapter
import com.fineweather.android.ui.place.PlaceViewModel
import kotlinx.android.synthetic.main.activity_location_search.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.thread


class LocationSearchActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter:PlaceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_search)
        //不在activity生命周期中进行获取焦点和弹出软键盘
        val imm:InputMethodManager= this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val size5=LocationSearchCHENGDU.textSize
        LogUtil.d("searcheditable",size5.toString())
        searchPlaceEdit.textSize=14.0f
        searchPlaceEdit.postDelayed(Runnable() {
            searchPlaceEdit.requestFocus()
            val imm:InputMethodManager= this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        },500)
        //Recycle的赋值，设置livedata观察对象 更改activity->this
        val layoutManager=LinearLayoutManager(this)
        LocationSearchRecyclerview.layoutManager=layoutManager
        adapter= PlaceAdapter(this,viewModel.placelise)
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
        LocationSearchCHANGSHA.setOnClickListener { searchPlaceEdit.setText("长沙市")
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