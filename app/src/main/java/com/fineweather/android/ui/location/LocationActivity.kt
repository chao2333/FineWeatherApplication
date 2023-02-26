package com.fineweather.android.ui.location

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fineweather.android.R
import com.fineweather.android.logic.dao.LogUtil
import com.fineweather.android.logic.model.LocationSaveItem2
import com.fineweather.android.ui.CustomDensityUtil
import com.fineweather.android.ui.LocationSaveAdapter
import kotlinx.android.synthetic.main.activity_location.*
import kotlin.concurrent.thread

class LocationActivity : AppCompatActivity() {
    private lateinit var locationViewModel: LocationViewModel
    @SuppressLint("Range")
    private lateinit var adapter: LocationSaveAdapter
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        locationViewModel=ViewModelProvider(this).get(LocationViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        //进行屏幕适配
        CustomDensityUtil.setCustomDensity(this,application)
        backMainActivity.setOnClickListener {
            finish()
        }
        searchPlaceButton.setOnClickListener {
            val intentToSearch=Intent(this,LocationSearchActivity::class.java)
            val options=ActivityOptionsCompat.makeSceneTransitionAnimation(this,searchPlaceButton,"SearchTransitionAnimation")
            startActivity(intentToSearch,options.toBundle())
        }
        //以下是recyclelist代码
        val LocationList=getList()
        val layoutManager=LinearLayoutManager(this)
        LocationSearchListview.layoutManager=layoutManager
        adapter=LocationSaveAdapter(this,LocationList,locationViewModel.database)
        LogUtil.d("databasesavetest1",LocationList.toString())
        LocationSearchListview.adapter=adapter

    }

    override fun onRestart() {
        LogUtil.d("databasesavetest","onRestart1111111")
        val LocationList=getList()
        val layoutManager=LinearLayoutManager(this)
        LocationSearchListview.layoutManager=layoutManager
        adapter=LocationSaveAdapter(this,LocationList,locationViewModel.database)
        LogUtil.d("databasesavetest1",LocationList.toString())
        LocationSearchListview.adapter=adapter
        super.onRestart()
    }
    @SuppressLint("Range")
    fun getList():ArrayList<LocationSaveItem2>{
        var acc:String;var rou:String;
        var lat:String;
        var lng:String;
        var sourceType:Int
        val returnList=ArrayList<LocationSaveItem2>()
        thread {
            locationViewModel.database.queryAll().let {
                for (i in it){
                    acc=i.AccurateLocation
                    rou=i.RoughLocation
                    lat=i.lat
                    lng=i.lng
                    sourceType=i.sourcetype
                    returnList.add(LocationSaveItem2(rou,acc,lat,lng,sourceType))
                }
            }
        }
        return returnList
        /*
        //没有使用ROOM数据库之前的逻辑
        val dbHelperWrite=SaveLocationDatabase(this,"LocationSave.db",1).writableDatabase
        val cursor=dbHelperWrite.query("Location",null,null,null,null,null,null)
        val LocationList= ArrayList<LocationSaveItem>()
        if(cursor.moveToFirst()){
            do {
                val acc=cursor.getString(cursor.getColumnIndex("AccurateLocation"))
                val rou=cursor.getString(cursor.getColumnIndex("RoughLocation"))
                val lat=cursor.getString(cursor.getColumnIndex("lat"))
                val lng=cursor.getString(cursor.getColumnIndex("lng"))
                val item=LocationSaveItem(rou,acc,lat,lng)
                LocationList.add(item)
                LogUtil.d("databasesavetest",item.toString())
            }while (cursor.moveToNext())
        }
        cursor.close()
        return LocationList

         */
    }
}