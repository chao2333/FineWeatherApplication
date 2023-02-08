package com.fineweather.android.logic.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SaveLocationDatabase(val context: Context,name:String,version:Int):
            SQLiteOpenHelper(context,name,null,version){

    private val creatSaveLocationDatabase by lazy {
        "CREATE TABLE Location("+
                "id integer PRIMARY KEY AUTOINCREMENT,"+
                "AccurateLocation text,"+
                "RoughLocation text,"+
                "lat text,"+
                "sourcetype INTEGER,"+
                "lng text)"
    }
    //private val InsertSaveLocation="INSERT INTO Location(AccurateLocation,RoughLocation,lat,lng) VALUES('${AccurateLocation}','${RoughLocation}','${lat}','${lng}')"
    // 主构造参数,AccurateLocation:String,RoughLocation:String,lat:String,lng:String
    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL(creatSaveLocationDatabase)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}