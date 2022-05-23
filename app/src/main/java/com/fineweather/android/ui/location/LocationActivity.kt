package com.fineweather.android.ui.location

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fineweather.android.R
import com.fineweather.android.logic.dao.LogUtil
import com.fineweather.android.logic.dao.SaveLocationDatabase
import com.fineweather.android.logic.model.LocationSaveItem
import com.fineweather.android.ui.LocationSaveAdapter
import kotlinx.android.synthetic.main.activity_location.*

class LocationActivity : AppCompatActivity() {
    @SuppressLint("Range")
    private lateinit var adapter: LocationSaveAdapter
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
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
        adapter=LocationSaveAdapter(this,LocationList)
        LogUtil.d("databasesavetest1",LocationList.toString())
        LocationSearchListview.adapter=adapter

    }

    override fun onRestart() {
        LogUtil.d("databasesavetest","onRestart1111111")
        val LocationList=getList()
        val layoutManager=LinearLayoutManager(this)
        LocationSearchListview.layoutManager=layoutManager
        adapter=LocationSaveAdapter(this,LocationList)
        LogUtil.d("databasesavetest1",LocationList.toString())
        LocationSearchListview.adapter=adapter
        super.onRestart()
    }
    @SuppressLint("Range")
    fun getList():ArrayList<LocationSaveItem>{
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
    }
}