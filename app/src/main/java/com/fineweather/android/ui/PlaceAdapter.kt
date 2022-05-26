package com.fineweather.android.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.FineWeatherApplication.Companion.context
import com.fineweather.android.R
import com.fineweather.android.logic.dao.LogUtil
import com.fineweather.android.logic.dao.SaveLocationDatabase
import com.fineweather.android.logic.model.Daily
import com.fineweather.android.logic.model.Hourly
import com.fineweather.android.logic.model.LocationSaveItem
import com.fineweather.android.logic.model.Place
import kotlinx.android.synthetic.main.locationsave_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class PlaceAdapter(private val activity:Activity,private val placeList:List<Place>):
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val placeName:TextView=view.findViewById(R.id.placeName)
        val placeAddress:TextView=view.findViewById(R.id.placeAddress)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LogUtil.d("onviewholder","privrous viewholder has creat")
        val view= LayoutInflater.from(parent.context).inflate(R.layout.locationsearch_item,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val place=placeList[position]
        holder.placeName.text=place.name
        holder.placeAddress.text=place.address
        //为每一个卡片设置点击事件
        holder.itemView.setOnClickListener {
            val position=holder.bindingAdapterPosition
            val location=placeList[position].location
            val dbHelper=SaveLocationDatabase(activity,"LocationSave.db",1)
            val db=dbHelper.writableDatabase
            val insert1=ContentValues().apply {
                put("AccurateLocation",place.address)
                put("RoughLocation",place.name)
                put("lat",place.location.lat)
                put("lng",place.location.lng)
                activity.onBackPressed()
            }
            db.insert("Location",null,insert1)
            Toast.makeText(FineWeatherApplication.context,"已经添加到我的城市",Toast.LENGTH_LONG).show()
            dbHelper.close()
        }
    }
    override fun getItemCount()=placeList.size
}


class LocationSaveAdapter(private val activity:Activity,private val savelist: ArrayList<LocationSaveItem>):
    RecyclerView.Adapter<LocationSaveAdapter.LocationSaveHolder>(){
    inner class LocationSaveHolder(View:View):RecyclerView.ViewHolder(View){
        val locationSaveCardRough:TextView=View.findViewById(R.id.LocationSaveCardRough)
        val locationSaveCardAccu:TextView=View.findViewById(R.id.LocationSaveCardAccu)
        val locationSaveImageView:ImageView=View.findViewById(R.id.LocationSaveCardImageview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationSaveHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.locationsave_item,parent,false)
        return LocationSaveHolder(view)
    }

    @SuppressLint("Range", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: LocationSaveHolder, position: Int) {
        val position2=holder.layoutPosition
        val location=savelist[position2]
        holder.locationSaveCardAccu.text=location.address
        holder.locationSaveCardRough.text=location.name
        holder.itemView.setOnLongClickListener {
            holder.locationSaveImageView.visibility=View.VISIBLE
            false
        }
        holder.itemView.setOnClickListener {
            //设置卡片背景
            // it.LocationSaveCardbk.setBackgroundResource(R.drawable.mainsunny)

            val pers=FineWeatherApplication.context.getSharedPreferences("ApplicationData",0)
            val persedit=pers.edit()
            persedit.putString("name",location.name)
            persedit.putString("address",location.address)
            persedit.putString("lat",location.lat)
            persedit.putString("lng",location.lng)
            //来源为搜索，false   来源为定位为true
            persedit.putBoolean("sourcetype",false)
            persedit.apply()
            activity.finish()
            Toast.makeText(activity,"切换地点为${location.name}",Toast.LENGTH_LONG).show()
        }
        //删除当前item
        holder.locationSaveImageView.setOnClickListener {
            val dbHelperWrite=SaveLocationDatabase(activity,"LocationSave.db",1).writableDatabase
            dbHelperWrite.execSQL("DELETE FROM Location WHERE RoughLocation='${location.name}'")
            savelist.remove(LocationSaveItem(location.name,location.address,location.lat,location.lng))
            notifyItemRemoved(position2)
            notifyDataSetChanged()
            holder.locationSaveImageView.visibility=GONE
        }
    }
    override fun getItemCount(): Int {
        return savelist.size
    }

}


class fifteenDayWeatherAdapter(val hourly: Hourly):
        RecyclerView.Adapter<fifteenDayWeatherAdapter.ViewHolder>(){

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val Time:TextView=view.findViewById(R.id.fifteenDayListviewTime)
        val Temperature:TextView=view.findViewById(R.id.fifteenDayListviewTemperature)
        val TemIcon:ImageView=view.findViewById(R.id.fifteenDayListviewTemIcon)
        val Wind:TextView=view.findViewById(R.id.fifteenDayListviewWind)
        val Air:TextView=view.findViewById(R.id.fifteenDayListviewAir)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): fifteenDayWeatherAdapter.ViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.main_fifteendayweatherlistviewitem,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: fifteenDayWeatherAdapter.ViewHolder, position: Int) { //每个item滚动到屏幕内执行
        val Time=hourly.temperature[position].datetime.split("T","+")
        val fomate=DecimalFormat("#")
        val Temperature=fomate.format(hourly.temperature[position].value).toString()
        val Skycon=hourly.skycon[position].value
        val wind=hourly.wind[position].speed
        val air=hourly.air_quality.aqi[position].value.chn

        holder.Time.text=Time[1]
        holder.Temperature.text=Temperature+ context.getString(R.string.mainthreedaytemperaturemaxandmin2)
        when(Skycon){
            "CLEAR_DAY" -> holder.TemIcon.setImageResource(R.drawable.ic_weather_clear_day)
            "CLEAR_NIGHT" -> holder.TemIcon.setImageResource(R.drawable.ic_weather_clear_night)
            "PARTLY_CLOUDY_DAY" -> holder.TemIcon.setImageResource(R.drawable.ic_weather_partly_cloudy_day)
            "PARTLY_CLOUDY_NIGHT" ->holder.TemIcon.setImageResource(R.drawable.ic_weather_partly_cloudy_night)
            "CLOUDY"->holder.TemIcon.setImageResource(R.drawable.ic_weather_cloudy)
            "LIGHT_HAZE"->holder.TemIcon.setImageResource(R.drawable.ic_weather_haze)
            "MODERATE_HAZE"->holder.TemIcon.setImageResource(R.drawable.ic_weather_haze)
            "HEAVY_HAZE"->holder.TemIcon.setImageResource(R.drawable.ic_weather_haze)
            "LIGHT_RAIN"->holder.TemIcon.setImageResource(R.drawable.ic_weather_light_rain)
            "MODERATE_RAIN"->holder.TemIcon.setImageResource(R.drawable.ic_weather_moderate_rain)
            "HEAVY_RAIN"->holder.TemIcon.setImageResource(R.drawable.ic_weather_heavy_rain)
            "STORM_RAIN"->holder.TemIcon.setImageResource(R.drawable.ic_weather_storm_rain)
            "FOG"->holder.TemIcon.setImageResource(R.drawable.ic_weather_fog)
            "LIGHT_SNOW"->holder.TemIcon.setImageResource(R.drawable.ic_weather_light_snow)
            "MODERATE_SNOW"->holder.TemIcon.setImageResource(R.drawable.ic_weather_moderate_snow)
            "HEAVY_SNOW"->holder.TemIcon.setImageResource(R.drawable.ic_weather_heavy_snow)
            "STORM_SNOW"->holder.TemIcon.setImageResource(R.drawable.ic_weather_storm_snow)
            "DUST"->holder.TemIcon.setImageResource(R.drawable.ic_weather_dust)
            "SAND"->holder.TemIcon.setImageResource(R.drawable.ic_weather_sand)
            "WIND"->holder.TemIcon.setImageResource(R.drawable.ic_weather_wind)
        }
        when(wind){
            in 0.0..1.0 -> holder.Wind.text =context.getString(R.string.aa级)
            in 1.1..5.5 ->holder.Wind.text =context.getString(R.string.ab级)
            in 5.6..11.5 ->holder.Wind.text =context.getString(R.string.ac级)
            in 11.6..19.5 ->holder.Wind.text =context.getString(R.string.ad级)
            in 19.6..28.5 ->holder.Wind.text =context.getString(R.string.ae级)
            in 28.6..38.5 ->holder.Wind.text =context.getString(R.string.af级)
            in 38.6..49.5 ->holder.Wind.text =context.getString(R.string.ag级)
            in 49.6..61.5 ->holder.Wind.text =context.getString(R.string.ah级)
            in 61.6..74.5 ->holder.Wind.text =context.getString(R.string.ai级)
            in 74.6..88.5 ->holder.Wind.text =context.getString(R.string.aj级)
            in 88.6..102.5 ->holder.Wind.text =context.getString(R.string.ak级)
            in 102.6..117.5 ->holder.Wind.text =context.getString(R.string.al级)
            in 117.5..148.5 ->holder.Wind.text =context.getString(R.string.am级)
            in 148.6..500.1 ->holder.Wind.text =context.getString(R.string.an级)
        }
        when(air){
            in 0.0..50.0 ->holder.Air.text= context.getString(R.string.a优)
            in 50.1..100.0 ->holder.Air.text= context.getString(R.string.a良)
            in 100.1..150.0 ->holder.Air.text= context.getString(R.string.a轻度污染)
            in 150.1..200.0 ->holder.Air.text= context.getString(R.string.a中度污染)
            in 200.1..300.0 ->holder.Air.text= context.getString(R.string.a重度污染)
            in 300.1..1000.0 ->holder.Air.text= context.getString(R.string.a严重污染)
        }
    }

    override fun getItemCount()=24

}
































