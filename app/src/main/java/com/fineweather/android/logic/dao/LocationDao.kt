package com.fineweather.android.logic.dao

import android.content.Context
import androidx.room.*
import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.logic.model.LocationEntity

@Dao
interface LocationDao {
    @Insert
    fun insert(location:LocationEntity):Long

    @Update
    fun update(location: LocationEntity)

    @Delete
    fun delete(location: LocationEntity)

    @Query("delete from LocationEntity where lat=:lat and lng=:lng")
    fun deleteOne(lat:String,lng:String)

    @Query("select id from LocationEntity where lat=:lat and lng=:lng")
    fun queryId(lat:String,lng:String):Long
    @Query("select * from LocationEntity")
    fun queryAll():List<LocationEntity>

    @Query("select sourcetype from LocationEntity where lat=:lat")
    fun queryLat(lat:String):Int

    @Query("delete from LocationEntity where sourcetype=1")
    fun deleteAll()
}

@Database(version = 1, entities = [LocationEntity::class])
abstract class LocationDatabase:RoomDatabase(){
    abstract fun locationDao():LocationDao

    companion object{
        private var instance:LocationDatabase?=null

        @Synchronized
        fun getLocationDatabase():LocationDatabase{
            instance?.let {
                return it
            }
            return Room.databaseBuilder(FineWeatherApplication.context,LocationDatabase::class.java,"Location").build()
                .apply {
                    instance=this
                }
        }
    }
}