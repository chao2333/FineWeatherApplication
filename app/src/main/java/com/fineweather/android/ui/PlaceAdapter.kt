package com.fineweather.android.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.os.Build
import android.util.Log
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
import androidx.room.Dao
import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.FineWeatherApplication.Companion.context
import com.fineweather.android.MainActivity
import com.fineweather.android.R
import com.fineweather.android.logic.Respository
import com.fineweather.android.logic.Respository.refreshWeather
import com.fineweather.android.logic.dao.LocationDao
import com.fineweather.android.logic.dao.LocationDatabase
import com.fineweather.android.logic.dao.LogUtil
import com.fineweather.android.logic.dao.SaveLocationDatabase
import com.fineweather.android.logic.model.*
import com.fineweather.android.ui.place.MainViewModel
import com.fineweather.android.ui.place.PlaceViewModel
import kotlinx.android.synthetic.main.activity_location_search.*
import java.text.DecimalFormat
import java.util.*
import kotlin.concurrent.thread


class PlaceAdapter(private val activity:Activity,private val placeList:List<Place>,private val firstload:Int,private val database: LocationDao):
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
        //为每一个卡片设置点击事件
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
            thread {
                database.insert(LocationEntity(place.address,place.name,place.location.lat,place.location.lng,0))
            }
            Toast.makeText(FineWeatherApplication.context,"已经添加到我的城市",Toast.LENGTH_LONG).show()
            activity.onBackPressed()
            /*//没使用ROOM优化的代码
            val dbHelper=SaveLocationDatabase(activity,"LocationSave.db",1)
            val db=dbHelper.writableDatabase
            val insert1=ContentValues().apply {
                put("AccurateLocation",place.address)
                put("RoughLocation",place.name)
                put("lat",place.location.lat)
                put("lng",place.location.lng)
                put("sourcetype",0)

            }
            db.insert("Location",null,insert1)
            dbHelper.close()
             */


        }
    }
    override fun getItemCount()=placeList.size
}


class LocationSaveAdapter(private val activity:Activity,private val savelist: ArrayList<LocationSaveItem2>,private val database:LocationDao):
    RecyclerView.Adapter<LocationSaveAdapter.LocationSaveHolder>(){
  //使用ROOM优化  val db=SaveLocationDatabase(activity,"LocationSave.db",1)
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
        val sourceType=location.sourceType
        holder.itemView.setOnLongClickListener {
            holder.locationSaveImageView.visibility=View.VISIBLE
            false
        }
        holder.itemView.setOnClickListener {
            //设置卡片背景
            val pers=context.getSharedPreferences("ApplicationData",0)
            val persedit=pers.edit()
            persedit.putString("name",location.name)
            persedit.putString("address",location.address)
            persedit.putString("lat",location.lat)
            persedit.putString("lng",location.lng)
            val location9=location.lng+","+location.lat
            Respository.refreshWeather(location9)
            //来源为搜索，0   来源为定位为1
            thread {
                persedit.putInt("sourcetype",database.queryLat(location.lat))
                persedit.apply()
                LogUtil.d("sourcetype",database.queryLat(location.lat).toString())
            }

 /*使用ROOM优化
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
  */
            persedit.apply()
            activity.finish()
            Toast.makeText(activity,"切换地点为${location.name}",Toast.LENGTH_LONG).show()
        }
        //删除当前item
        holder.locationSaveImageView.setOnClickListener {
            /*使用ROOM优化
            val dbHelperWrite=db.writableDatabase
            dbHelperWrite.execSQL("DELETE FROM Location WHERE RoughLocation='${location.name}'")

             */
            thread {
                database.deleteOne(location.lat,location.lng)
            }
            savelist.remove(LocationSaveItem2(location.name,location.address,location.lat,location.lng,location.sourceType))
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

class FifteenDayLayoutAdapter(val daily: Daily,val activity: Activity):
    RecyclerView.Adapter<FifteenDayLayoutAdapter.ViewHolder>(){
    private val humidity1=activity.findViewById<TextView>(R.id.fifteenday_detail_humidity)
    private val visibility1=activity.findViewById<TextView>(R.id.fifteenday_detail_visibility)
    private val pressure1=activity.findViewById<TextView>(R.id.fifteenday_detail_pressure)
    private val cloudrate1=activity.findViewById<TextView>(R.id.fifteenday_detail_cloudrate)
    private val fifteenday_detail_date=activity.findViewById<TextView>(R.id.fifteenday_detail_date)
    private var currentPosition=0
    private val week= arrayListOf("今天")
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
                    1 -> week.add("周日")
                    2 -> week.add("周一")
                    3 -> week.add("周二")
                    4 -> week.add("周三")
                    5 -> week.add("周四")
                    6 -> week.add("周五")
                    0 -> week.add("周六")
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
        //设置星期几
        holder.date.text=week[position]
        //设置几月几日
        val mad=daily.temperature[position].date.split("-","T")
        var month=mad[1]
        var day=mad[2]
        if (month[0]=='0'){ month=month[1].toString() }
        if (day[0]=='0'){ day=day[1].toString() }
        holder.date2.text=month+"月"+day+"日"
        //设置天气描述和图标
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
        //设置最高温度和最低温度
        holder.high.text=daily.temperature[position].max.toInt().toString()+ context.getString(R.string.maintoptemperature2)
        holder.low.text=daily.temperature[position].min.toInt().toString()+ context.getString(R.string.maintoptemperature2)
        //设置风速
        when(daily.wind[position].max.speed){
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
        //设置空气污染和背景
        when(daily.air_quality.aqi[position].max.chn){
            in 0.0..50.0 -> {
                holder.Air.text = context.getString(R.string.a优)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.Air.background= AppCompatResources.getDrawable(context,R.drawable.fifteenday_item_airquality_bk_green)
                }
            }
            in 50.1..100.0 -> {
                holder.Air.text = context.getString(R.string.a良)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.Air.background= AppCompatResources.getDrawable(context,R.drawable.fifteenday_item_airquality_bk_yellow)
                }
            }
            in 100.1..150.0 -> {
                holder.Air.text = context.getString(R.string.a轻度污染)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.Air.background= AppCompatResources.getDrawable(context,R.drawable.fifteenday_item_airquality_bk_orange)
                }
            }
            in 150.1..200.0 -> {
                holder.Air.text = context.getString(R.string.a中度污染)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.Air.background= AppCompatResources.getDrawable(context,R.drawable.fifteenday_item_airquality_bk_red)
                }
            }
            in 200.1..300.0 -> {
                holder.Air.text = context.getString(R.string.a重度污染)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.Air.background= AppCompatResources.getDrawable(context,R.drawable.fifteenday_item_airquality_bk_purple)
                }
            }
            in 300.1..1000.0 -> {
                holder.Air.text = context.getString(R.string.a严重污染)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.Air.background= AppCompatResources.getDrawable(context,R.drawable.fifteenday_item_airquality_bk_brown)
                }
            }
        }
        //设置每一个item点击事件
        if(currentPosition==position){
            holder.itemView.setBackgroundResource(R.drawable.fifteenday_item_select_bk)
            fifteenday_detail_date.text=month+"月"+day+"日"
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
































