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


class LocationSearchActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter:PlaceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_search)
        //更改activity->this
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