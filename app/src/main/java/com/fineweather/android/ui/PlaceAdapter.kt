package com.fineweather.android.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.FineWeatherApplication.Companion.context
import com.fineweather.android.MainActivity
import com.fineweather.android.R
import com.fineweather.android.logic.Respository
import com.fineweather.android.logic.Respository.refreshWeather
import com.fineweather.android.logic.dao.LogUtil
import com.fineweather.android.logic.dao.SaveLocationDatabase
import com.fineweather.android.logic.model.*
import com.fineweather.android.ui.place.MainViewModel
import java.text.DecimalFormat
import java.util.*


class PlaceAdapter(private val activity:Activity,private val placeList:List<Place>,private val firstload:Int):
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    val pers= Respository.getSqlite()
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
        //????????????????????????????????????
        holder.itemView.setOnClickListener {
            val position=holder.bindingAdapterPosition
            val location=placeList[position].location
            if (firstload==1&&pers!=null){
                val edit=pers.edit()
                edit.putString("lng",place.location.lng)
                edit.putString("lat",place.location.lat)
                edit.putString("name",place.name)
                edit.putInt("sourcetype",0)
                edit.apply()
            }
            val dbHelper=SaveLocationDatabase(activity,"LocationSave.db",1)
            val db=dbHelper.writableDatabase
            val insert1=ContentValues().apply {
                put("AccurateLocation",place.address)
                put("RoughLocation",place.name)
                put("lat",place.location.lat)
                put("lng",place.location.lng)
                put("sourcetype",0)
                activity.onBackPressed()
            }
            db.insert("Location",null,insert1)
            Toast.makeText(FineWeatherApplication.context,"???????????????????????????",Toast.LENGTH_LONG).show()
            dbHelper.close()
        }
    }
    override fun getItemCount()=placeList.size
}


class LocationSaveAdapter(private val activity:Activity,private val savelist: ArrayList<LocationSaveItem>):
    RecyclerView.Adapter<LocationSaveAdapter.LocationSaveHolder>(){
    val db=SaveLocationDatabase(activity,"LocationSave.db",1)
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
            //??????????????????
            val pers=context.getSharedPreferences("ApplicationData",0)
            val persedit=pers.edit()
            persedit.putString("name",location.name)
            persedit.putString("address",location.address)
            persedit.putString("lat",location.lat)
            persedit.putString("lng",location.lng)
            val location9=location.lng+","+location.lat
            Respository.refreshWeather(location9)
            //??????????????????0   ??????????????????1
            var result=1
            val cursor=db.writableDatabase.query("Location", arrayOf("sourcetype"),"lat=?", arrayOf(location.lat),null,null,null)
            if(cursor.moveToNext()){
                do {
                    result=cursor.getInt(cursor.getColumnIndex("sourcetype"))
                    break
                }while (cursor.moveToNext())
            }
            if (result==0||result==1){
                persedit.putInt("sourcetype",result)
            }else{
                persedit.putInt("sourcetype",1)
            }
            cursor.close()
            persedit.apply()
            activity.finish()
            Toast.makeText(activity,"???????????????${location.name}",Toast.LENGTH_LONG).show()
        }
        //????????????item
        holder.locationSaveImageView.setOnClickListener {
            val dbHelperWrite=db.writableDatabase
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

    override fun onBindViewHolder(holder: fifteenDayWeatherAdapter.ViewHolder, position: Int) { //??????item????????????????????????
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
            in 0.0..1.0 -> holder.Wind.text =context.getString(R.string.aa???)
            in 1.1..5.5 ->holder.Wind.text =context.getString(R.string.ab???)
            in 5.6..11.5 ->holder.Wind.text =context.getString(R.string.ac???)
            in 11.6..19.5 ->holder.Wind.text =context.getString(R.string.ad???)
            in 19.6..28.5 ->holder.Wind.text =context.getString(R.string.ae???)
            in 28.6..38.5 ->holder.Wind.text =context.getString(R.string.af???)
            in 38.6..49.5 ->holder.Wind.text =context.getString(R.string.ag???)
            in 49.6..61.5 ->holder.Wind.text =context.getString(R.string.ah???)
            in 61.6..74.5 ->holder.Wind.text =context.getString(R.string.ai???)
            in 74.6..88.5 ->holder.Wind.text =context.getString(R.string.aj???)
            in 88.6..102.5 ->holder.Wind.text =context.getString(R.string.ak???)
            in 102.6..117.5 ->holder.Wind.text =context.getString(R.string.al???)
            in 117.5..148.5 ->holder.Wind.text =context.getString(R.string.am???)
            in 148.6..500.1 ->holder.Wind.text =context.getString(R.string.an???)
        }
        when(air){
            in 0.0..50.0 ->holder.Air.text= context.getString(R.string.a???)
            in 50.1..100.0 ->holder.Air.text= context.getString(R.string.a???)
            in 100.1..150.0 ->holder.Air.text= context.getString(R.string.a????????????)
            in 150.1..200.0 ->holder.Air.text= context.getString(R.string.a????????????)
            in 200.1..300.0 ->holder.Air.text= context.getString(R.string.a????????????)
            in 300.1..1000.0 ->holder.Air.text= context.getString(R.string.a????????????)
        }

    }

    override fun getItemCount()=24

}

class FifteenDayLayoutAdapter(val daily: Daily,val activity: Activity):
    RecyclerView.Adapter<FifteenDayLayoutAdapter.ViewHolder>(){
    private val humidity1=activity.findViewById<TextView>(R.id.fifteenday_detail_humidity)
    private val visibility1=activity.findViewById<TextView>(R.id.fifteenday_detail_visibility)
    private val pressure1=activity.findViewById<TextView>(R.id.fifteenday_detail_pressure)
    private val cloudrate1=activity.findViewById<TextView>(R.id.fifteenday_detail_cloudrate)
    private val fifteenday_detail_date=activity.findViewById<TextView>(R.id.fifteenday_detail_date)
    private var currentPosition=0
    private val week= arrayListOf("??????")
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val date:TextView=view.findViewById(R.id.fifteenday_recycler_item_date)
        val date2:TextView=view.findViewById(R.id.fifteenday_recycler_item_date2)
        val skycon:TextView=view.findViewById(R.id.fifteenday_recycler_item_skycon)
        val icon:ImageView=view.findViewById(R.id.fifteenday_recycler_item_icon)
        val high:TextView=view.findViewById(R.id.fifteenday_recycler_item_high)
        val low:TextView=view.findViewById(R.id.fifteenday_recycler_item_low)
        val Wind:TextView=view.findViewById(R.id.fifteenday_recycler_item_wind)
        val Air:TextView=view.findViewById(R.id.fifteenday_recycler_item_pollution)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FifteenDayLayoutAdapter.ViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.fifteendays_recyclerview_item,parent,false)

        val a= Calendar.getInstance()
        var b=a.get(Calendar.DAY_OF_WEEK)
        if(b==1||b==2||b==3||b==4||b==5||b==6||b==7) {
            b += 1
            for (i in 0 until 14) {
                b %= 7
                when (b) {
                    1 -> week.add("??????")
                    2 -> week.add("??????")
                    3 -> week.add("??????")
                    4 -> week.add("??????")
                    5 -> week.add("??????")
                    6 -> week.add("??????")
                    0 -> week.add("??????")
                }
                b += 1
            }
        }else{
            for (i in 0 until 14) {
                week.add(" ")
            }
        }
        return ViewHolder(view)


    }
    override fun onBindViewHolder(holder: FifteenDayLayoutAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        //???????????????
        holder.date.text=week[position]
        //??????????????????
        val mad=daily.temperature[position].date.split("-","T")
        var month=mad[1]
        var day=mad[2]
        if (month[0]=='0'){ month=month[1].toString() }
        if (day[0]=='0'){ day=day[1].toString() }
        holder.date2.text=month+"???"+day+"???"
        //???????????????????????????
        when(daily.skycon[position].value){
            "CLEAR_DAY" -> {
                holder.icon.setImageResource(R.drawable.ic_weather_clear_day)
                holder.skycon.text=context.getString(R.string.mainthreeday1012)
            }
            "CLEAR_NIGHT" -> {
                holder.icon.setImageResource(R.drawable.ic_weather_clear_night)
                holder.skycon.text=context.getString(R.string.mainthreeday1012)
            }
            "PARTLY_CLOUDY_DAY" -> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_partly_cloudy_day)
                holder.skycon.text=context.getString(R.string.mainthreeday1022)
            }
            "PARTLY_CLOUDY_NIGHT" -> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_partly_cloudy_night)
                holder.skycon.text=context.getString(R.string.mainthreeday1022)
            }
            "CLOUDY"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_cloudy)
                holder.skycon.text=context.getString(R.string.mainthreeday1032)
            }
            "LIGHT_HAZE"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_haze)
                holder.skycon.text=context.getString(R.string.mainthreeday1042)

            }
            "MODERATE_HAZE"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_haze)
                holder.skycon.text=context.getString(R.string.mainthreeday1052)
            }
            "HEAVY_HAZE"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_haze)
                holder.skycon.text=context.getString(R.string.mainthreeday1062)
            }
            "LIGHT_RAIN"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_light_rain)
                holder.skycon.text=context.getString(R.string.mainthreeday1072)
            }
            "MODERATE_RAIN"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_moderate_rain)
                holder.skycon.text=context.getString(R.string.mainthreeday1082)
            }
            "HEAVY_RAIN"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_heavy_rain)
                holder.skycon.text=context.getString(R.string.mainthreeday1092)
            }
            "STORM_RAIN"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_storm_rain)
                holder.skycon.text=context.getString(R.string.mainthreeday1102)
            }
            "FOG"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_fog)
                holder.skycon.text=context.getString(R.string.mainthreeday1112)
            }
            "LIGHT_SNOW"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_light_snow)
                holder.skycon.text=context.getString(R.string.mainthreeday1122)
            }
            "MODERATE_SNOW"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_moderate_snow)
                holder.skycon.text=context.getString(R.string.mainthreeday1132)
            }

            "HEAVY_SNOW"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_heavy_snow)
                holder.skycon.text=context.getString(R.string.mainthreeday1142)
            }
            "STORM_SNOW"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_storm_snow)
                holder.skycon.text=context.getString(R.string.mainthreeday1152)
            }
            "DUST"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_dust)
                holder.skycon.text=context.getString(R.string.mainthreeday1162)
            }
            "SAND"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_sand)
                holder.skycon.text=context.getString(R.string.mainthreeday1172)
            }
            "WIND"-> {
                holder.icon.setImageResource(R.drawable.ic_weather_black_wind)
                holder.skycon.text=context.getString(R.string.mainthreeday1182)
            }
        }
        //?????????????????????????????????
        holder.high.text=daily.temperature[position].max.toInt().toString()+ context.getString(R.string.maintoptemperature2)
        holder.low.text=daily.temperature[position].min.toInt().toString()+ context.getString(R.string.maintoptemperature2)
        //????????????
        when(daily.wind[position].max.speed){
            in 0.0..1.0 -> holder.Wind.text =context.getString(R.string.aa???)
            in 1.1..5.5 ->holder.Wind.text =context.getString(R.string.ab???)
            in 5.6..11.5 ->holder.Wind.text =context.getString(R.string.ac???)
            in 11.6..19.5 ->holder.Wind.text =context.getString(R.string.ad???)
            in 19.6..28.5 ->holder.Wind.text =context.getString(R.string.ae???)
            in 28.6..38.5 ->holder.Wind.text =context.getString(R.string.af???)
            in 38.6..49.5 ->holder.Wind.text =context.getString(R.string.ag???)
            in 49.6..61.5 ->holder.Wind.text =context.getString(R.string.ah???)
            in 61.6..74.5 ->holder.Wind.text =context.getString(R.string.ai???)
            in 74.6..88.5 ->holder.Wind.text =context.getString(R.string.aj???)
            in 88.6..102.5 ->holder.Wind.text =context.getString(R.string.ak???)
            in 102.6..117.5 ->holder.Wind.text =context.getString(R.string.al???)
            in 117.5..148.5 ->holder.Wind.text =context.getString(R.string.am???)
            in 148.6..500.1 ->holder.Wind.text =context.getString(R.string.an???)
        }
        //???????????????????????????
        when(daily.air_quality.aqi[position].max.chn){
            in 0.0..50.0 -> {
                holder.Air.text = context.getString(R.string.a???)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.Air.background= AppCompatResources.getDrawable(context,R.drawable.fifteenday_item_airquality_bk_green)
                }
            }
            in 50.1..100.0 -> {
                holder.Air.text = context.getString(R.string.a???)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.Air.background= AppCompatResources.getDrawable(context,R.drawable.fifteenday_item_airquality_bk_yellow)
                }
            }
            in 100.1..150.0 -> {
                holder.Air.text = context.getString(R.string.a????????????)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.Air.background= AppCompatResources.getDrawable(context,R.drawable.fifteenday_item_airquality_bk_orange)
                }
            }
            in 150.1..200.0 -> {
                holder.Air.text = context.getString(R.string.a????????????)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.Air.background= AppCompatResources.getDrawable(context,R.drawable.fifteenday_item_airquality_bk_red)
                }
            }
            in 200.1..300.0 -> {
                holder.Air.text = context.getString(R.string.a????????????)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.Air.background= AppCompatResources.getDrawable(context,R.drawable.fifteenday_item_airquality_bk_purple)
                }
            }
            in 300.1..1000.0 -> {
                holder.Air.text = context.getString(R.string.a????????????)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.Air.background= AppCompatResources.getDrawable(context,R.drawable.fifteenday_item_airquality_bk_brown)
                }
            }
        }
        //???????????????item????????????
        if(currentPosition==position){
            holder.itemView.setBackgroundResource(R.drawable.fifteenday_item_select_bk)
            fifteenday_detail_date.text=month+"???"+day+"???"
            humidity1.text=(daily.humidity[position].avg*100).toInt().toString()+ context.getString(R.string.baifenbi)
            visibility1.text=daily.visibility[position].avg.toInt().toString()+ context.getString(R.string.kilometer)
            pressure1.text=(daily.pressure[position].avg/100).toInt().toString()+ context.getString(R.string.main_sunriseandset4)
            cloudrate1.text=(daily.cloudrate[position].avg*100).toInt().toString()+ context.getString(R.string.baifenbi)
        }else{
            holder.itemView.setBackgroundResource(R.color.white)
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            currentPosition = position
            notifyDataSetChanged()
        })

    }
    override fun getItemCount()=15


}
































