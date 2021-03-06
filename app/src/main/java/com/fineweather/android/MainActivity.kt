package com.fineweather.android

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fineweather.android.FineWeatherApplication.Companion.context
import com.fineweather.android.logic.Respository
import com.fineweather.android.logic.dao.LogUtil
import com.fineweather.android.logic.dao.SaveLocationDatabase
import com.fineweather.android.logic.model.*
import com.fineweather.android.ui.*
import com.fineweather.android.ui.location.LocationActivity
import com.fineweather.android.ui.place.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_15dayweather.*
import kotlinx.android.synthetic.main.main_air_quality.*
import kotlinx.android.synthetic.main.main_earlywarninginformation.*
import kotlinx.android.synthetic.main.main_earlywarninginformation2.*
import kotlinx.android.synthetic.main.main_lifeindex.*
import kotlinx.android.synthetic.main.main_sunriseandset.*
import kotlinx.android.synthetic.main.main_temperature.*
import kotlinx.android.synthetic.main.main_threedayweatherbutton.*
import kotlinx.android.synthetic.main.main_threedayweatheritem.*
import kotlinx.android.synthetic.main.main_threedayweatheritem.view.*
import kotlinx.android.synthetic.main.main_threedayweatheritem2.*
import kotlinx.android.synthetic.main.main_threedayweatheritem2.view.*
import kotlinx.android.synthetic.main.main_threedayweatheritem3.*
import kotlinx.android.synthetic.main.main_threedayweatheritem3.view.*
import kotlinx.android.synthetic.main.main_threedayweatheritemgap.*
import kotlinx.android.synthetic.main.main_threedayweatheritemgap2.*
import kotlinx.android.synthetic.main.main_threedayweatheritemgap3.*
import kotlinx.android.synthetic.main.main_twohoursrain.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private var confirmupdate=""
    val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
    var haveChanged=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        haveChanged=0
        LogUtil.d("mainactivitytestbk",haveChanged.toString())
        //?????????????????????
        val applicationDataPers=viewModel.getSharepreferences()
        //???????????????????????????
        topTextView.text=applicationDataPers.getString("name","")
        //?????????????????????????????????????????????????????????
        val flag1=applicationDataPers.getInt("sourcetype",0)
        confirmupdate= applicationDataPers.getString("lat","").toString()
        var nowlng=""
        var nowlat=""
        if (flag1==1){//1??????????????????,????????????????????????,????????????
            topTextView.text=this.getString(R.string.upDataTextViewGPS)
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"???????????????????????????????????????????????????",Toast.LENGTH_LONG).show()
            }else{

                if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 8f, object :
                                LocationListener{
                                override fun onLocationChanged(p0: Location) {
                                    nowlng=p0.longitude.toString()
                                    nowlat=p0.latitude.toString()
                                    val edit2=applicationDataPers.edit()
                                    edit2.putString("lat",nowlat)
                                    edit2.putString("lng",nowlng)
                                    edit2.putString("name","????????????")
                                    confirmupdate= nowlat
                                    edit2.apply()
                                    locationManager.removeUpdates(this)
                                }
                                override fun onProviderDisabled(provider: String) {
                                }
                                override fun onProviderEnabled(provider: String) {
                                    // ???GPS LocationProvider????????????????????????
                                }
                                override fun onStatusChanged(
                                    provider: String, status: Int,
                                    extras: Bundle
                                ) {
                                }
                            })
                    }
                }
            }
        }
        val location8=applicationDataPers.getString("lng","")+","+applicationDataPers.getString("lat","")
        viewModel.refreshWeather(location8)
        //ViewModel??????????????????
        viewModel.weatherLiveData.observe(this, Observer { result->
            val weather=result.getOrNull()
            if (weather != null) {
                weatherLayout.visibility=View.VISIBLE
                swipeRefresh.isRefreshing = false
                LogUtil.d("onResumetest","refresh")
                //????????????????????????
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        upDataTextView.text=context.getString(R.string.space)
                    }
                }, 2500)
                upDataTextView.text=this.getString(R.string.upDataTextViewupdataing2)
                //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????   ????????????haveChanged=0???????????????????????????????????????1
                if (haveChanged==0){
                    //??????????????????????????????mainbk???0????????????????????????????????????????????????0????????????onResume?????????????????????????????????
                    val mainbk=applicationDataPers.getInt("mainbk",0)
                    if (mainbk==0){
                        val test1=(1..20).random()
                        val test2=(1..40).random()
                        LogUtil.d("mainactivitytestbk5",test1.toString())
                        when(weather.result.realtime.skycon){
                            "CLEAR_DAY","PARTLY_CLOUDY_DAY","LIGHT_HAZE","MODERATE_HAZE","HEAVY_HAZE","WIND"-> {
                                if(test1<11){
                                    when(test1){
                                        1-> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunny2)
                                        2->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunny3)
                                        3->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnyafternoon)
                                        4->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnyafternoon2)
                                        5->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnymountain)
                                        6->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnynocloud)
                                        7->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnypeace)
                                        8->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnypinkandblue)
                                        9->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnysmall)
                                        10->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_evening)
                                    }
                                }

                            }
                            "CLEAR_NIGHT","PARTLY_CLOUDY_NIGHT"-> {
                                when (test2) {
                                    1 -> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunny2)
                                    2 -> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunny3)
                                    3 -> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnyafternoon)
                                    4 -> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnyafternoon2)
                                    5 -> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnymountain)
                                    6 -> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnynocloud)
                                    7 -> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnypeace)
                                    8 -> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnypinkandblue)
                                    9 -> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnysmall)
                                    10 -> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_evening)
                                    11, 12, 13, 14, 15,16,17,18,19,20,21,22,23,24 -> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_night)
                                }
                            }
                            "LIGHT_RAIN","MODERATE_RAIN","HEAVY_RAIN","STORM_RAIN","FOG","CLOUDY"->{
                                if(test2<20){
                                    when(test1){
                                        1-> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunny2)
                                        2->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunny3)
                                        3->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnyafternoon)
                                        4->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnyafternoon2)
                                        5->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnymountain)
                                        6->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnynocloud)
                                        7->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnypeace)
                                        8->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnypinkandblue)
                                        9->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnysmall)
                                        10->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_evening)
                                        11,12,13,14,15,16,17,18,19,->coordinatorlayout.setBackgroundResource(R.drawable.mainrainy)
                                    }
                                }
                            }
                            "LIGHT_SNOW","MODERATE_SNOW","HEAVY_SNOW","STORM_SNOW"->{
                                if (test2<20){
                                    when(test2){
                                        1-> coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunny2)
                                        2->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunny3)
                                        3->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnyafternoon)
                                        4->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnyafternoon2)
                                        5->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnymountain)
                                        6->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnynocloud)
                                        7->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnypeace)
                                        8->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnypinkandblue)
                                        9->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnysmall)
                                        10->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_evening)
                                        11,12,13,14,15,16,17,18,19->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_snow)
                                    }
                                }
                            }
                            "DUST","SAND"->{
                                when(test2){
                                    1,2,3,4,5,6,7,8,9,10,18,19,20,21,22,23->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sand)
                                    11,12->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnyafternoon)
                                    14,15->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnysmall)
                                    16,17->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnymountain)
                                }

                            }
                        }
                    }
                    haveChanged =1
                    LogUtil.d("mainactivitytestbk",haveChanged.toString())
                }

                //????????????????????????
                showfifteendays(weather.result.hourly)
                showthreedays(weather.result.daily)
                showtop(weather.result.realtime.temperature,weather.result.realtime.skycon)
                showearlyWarningInformation(weather.result.alert)
                showairandultraviolet(weather.result.realtime)
                showsunriseandset(weather.result.daily.astro[0],weather.result.realtime.wind,weather.result.realtime.humidity,weather.result.realtime.apparent_temperature,weather.result.realtime.pressure)
                showlifeindex(weather.result.minutely.precipitation_2h,weather.result.daily.life_index.comfort[0],weather.result.daily.life_index.dressing[0],weather.result.daily.life_index.coldRisk[0].desc,weather.result.realtime.temperature,weather.result.realtime.skycon,weather.result.daily.life_index.carWashing[0].desc)
                //????????????????????????
                main_airqualitybutton.setOnClickListener {
                    val airqualitydata=Airqualitydata()
                    airqualitydata.time=weather.result.hourly.precipitation[0].datetime
                    airqualitydata.airquality=weather.result.realtime.air_quality.aqi.chn
                    airqualitydata.pm25=weather.result.realtime.air_quality.pm25
                    airqualitydata.pm10=weather.result.realtime.air_quality.pm10
                    airqualitydata.so2=weather.result.realtime.air_quality.so2
                    airqualitydata.no2=weather.result.realtime.air_quality.no2
                    airqualitydata.o3=weather.result.realtime.air_quality.o3
                    airqualitydata.co=weather.result.realtime.air_quality.co
                    val aqiarraylist=weather.result.hourly.air_quality.aqi
                    val aqiarraylist1=arrayListOf(0.0)
                    val timearraylist1= arrayListOf("")
                    for(index in 0..23){
                        aqiarraylist1.add(aqiarraylist[index].value.chn)
                        timearraylist1.add(aqiarraylist[index].datetime)
                    }
                    airqualitydata.aqichnlist=aqiarraylist1
                    airqualitydata.timearraylist=timearraylist1

                    LogUtil.d("mainactivitytest",aqiarraylist1.toString())
                    LogUtil.d("mainactivitytest",timearraylist1.toString())

                    val intent=Intent(this,MainAirQualityActivity::class.java)
                    intent.putExtra("Airqualitydata",airqualitydata)
                    startActivity(intent)
                }
                //?????????????????????
                mainthreedayweatheritem1.setOnClickListener { fifteendays(weather.result.daily,this) }
                mainthreedayweatheritem2.setOnClickListener { fifteendays(weather.result.daily,this)}
                mainthreedayweatheritem3.setOnClickListener { fifteendays(weather.result.daily,this) }
                mainthreedayweatheritem4.setOnClickListener { fifteendays(weather.result.daily,this)}
                //????????????????????????
                for(index in 0..119){
                    if(weather.result.minutely.precipitation_2h[index]!=0.0){
                        main_twohoursrain.visibility=View.VISIBLE
                        if (index>60){
                            twohoursrain_button.text=this.getString(R.string.main_twohours1)+(index-60).toString()+this.getString(R.string.main_twohours2)
                            break
                        }
                        if (index<=60){
                            if(index==0){
                                twohoursrain_button.text=this.getString(R.string.main_twohours3)
                                break
                            }else{
                                twohoursrain_button.text=(index).toString()+this.getString(R.string.main_twohours2)
                                break
                            }
                        }
                    }
                }
                twohoursrain_button.setOnClickListener {
                    val intent7=Intent(this,MainTwoHoursRainActivity::class.java)
                    intent7.putExtra("twohoursraindata",weather.result.minutely.precipitation_2h)
                    startActivity(intent7)
                }
                //????????????
                main_earlywarningb1_clickimage.setOnClickListener {
                    val intent8=Intent(this,MainAlertActivity::class.java)
                    intent8.putExtra("alertdata",weather.result.alert)
                    startActivity(intent8)
                }
                main_earlywarningb1_clickimage2.setOnClickListener {
                    val intent9=Intent(this,MainAlertActivity::class.java)
                    intent9.putExtra("alertdata",weather.result.alert)
                    startActivity(intent9)
                }
                //?????????????????????
                main_ultravioletbutton.setOnClickListener {
                    when(weather.result.realtime.life_index.ultraviolet.index.toInt()){
                        0,1,2-> Toast.makeText(this, "?????????????????????????????????~", Toast.LENGTH_LONG).show()
                        3,4->Toast.makeText(this, "?????????????????????????????????~", Toast.LENGTH_LONG).show()
                        5,6->Toast.makeText(this, "?????????????????????????????????????????????~", Toast.LENGTH_LONG).show()
                        7,8->Toast.makeText(this, "??????????????????????????????????????????~", Toast.LENGTH_LONG).show()
                        9,10->Toast.makeText(this, "?????????????????????????????????????????????~", Toast.LENGTH_LONG).show()
                        11,12,13,14,15->Toast.makeText(this, "???????????????????????????????????????", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                weatherLayout.visibility=View.GONE
                upDataTextView.text=context.getString(R.string.upDataTextViewupdataing3)
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        upDataTextView.text=context.getString(R.string.space)
                    }
                }, 2300)
                if(applicationDataPers.getInt("checkdatabase",0)>3){
                    Toast.makeText(this, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show()
                }
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        //??????????????????
        val firstload=applicationDataPers.getBoolean("Firstload",false)
        if(!firstload){
            val welcomeIntent=Intent(this,WelcomeActivity::class.java)
            startActivity(welcomeIntent)
        }
        //??????????????????
        topSettingButton.setOnClickListener{
            val settingintent= Intent(this, SettingActivity::class.java)
            startActivity(settingintent)
        }
        //??????????????????
        topLocationButton.setOnClickListener{
            val LocationIntent= Intent(this, LocationActivity::class.java)
            startActivity(LocationIntent)
        }
        //???????????????????????????
        swipeRefresh.setColorSchemeResources(R.color.Welcomewhileblack)
        //??????????????????
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
    }
    //????????????????????????
    fun refreshWeather(){
        val lng=Respository.getSqlite().getString("lng","")
        val lat=Respository.getSqlite().getString("lat","")
        if (lng != null&&lat!=null) {
            viewModel.refreshWeather("$lng,$lat")
        }
        swipeRefresh.isRefreshing = true
    }
    private fun fifteendays(daily:Daily,activity:Activity){
        val intent=Intent(activity,MainFifteenDaysActivity::class.java)
        intent.putExtra("dailydata",daily)
        startActivity(intent)
    }
    override fun onResume() {
        super.onResume()
        val pers=viewModel.getSharepreferences()
        LogUtil.d("onResumetest1",confirmupdate)
        LogUtil.d("onResumetest2",pers.getString("lat","").toString())
        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (confirmupdate!=pers.getString("lat","").toString()){
            confirmupdate=pers.getString("lat","").toString()
            val flag1=pers.getInt("sourcetype",0)
            var nowlng=""
            var nowlat=""
            if (flag1==1){//1??????????????????,????????????????????????,????????????
                val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"???????????????????????????????????????????????????",Toast.LENGTH_LONG).show()
                }else{

                    if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            if (location!=null){
                                nowlng=location.longitude.toString()
                                nowlat=location.latitude.toString()
                                val edit23=pers.edit()
                                edit23.putString("lat",nowlat)
                                edit23.putString("lng",nowlng)
                                edit23.putString("name","????????????")
                                edit23.apply()
                                LogUtil.d("welcomeactivitytest9", location.latitude.toString())
                            }
                            if(location==null){
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 8f, object :
                                    LocationListener{
                                    override fun onLocationChanged(p0: Location) {
                                        // ???GPS??????????????????????????????????????????
                                        //   LogUtil.d("nowLocationtest","??????"+p0.speed.toString()+",??????"+p0.bearing.toString()+",??????"+p0.longitude.toString()+",??????"+p0.latitude.toString())
                                        nowlng=p0.longitude.toString()
                                        nowlat=p0.latitude.toString()
                                        val edit2=pers.edit()
                                        edit2.putString("lat",nowlat)
                                        edit2.putString("lng",nowlng)
                                        edit2.putString("name","????????????")
                                        edit2.apply()
                                        locationManager.removeUpdates(this)
                                    }
                                    override fun onProviderDisabled(provider: String) {
                                    }
                                    override fun onProviderEnabled(provider: String) {
                                        // ???GPS LocationProvider????????????????????????
                                    }
                                    override fun onStatusChanged(
                                        provider: String, status: Int,
                                        extras: Bundle
                                    ) {
                                    }
                                })
                            }
                        }
                    }
                }
            }
            LogUtil.d("mainactivityonresumetest",nowlat+nowlng)
            val refreshstring=pers.getString("lng","")+","+pers.getString("lat","")
            LogUtil.d("mainactivityonresumetest",refreshstring)
            viewModel.refreshWeather(refreshstring)
            topTextView.text=pers.getString("name","")
        }
        //???????????????????????????
        val mainbk=pers.getInt("mainbk",0)
        if (mainbk!=0){
            when(mainbk){
                1->coordinatorlayout.setBackgroundResource(R.drawable.mainsunny)
                2->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_evening)
                3->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunny2)
                4->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunny3)
                5->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnyafternoon)
                6->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnyafternoon2)
                7->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnymountain)
                8->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnynocloud)
                9->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnypeace)
                10->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnypinkandblue)
                11->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sunnysmall)
                12->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_night)
                13->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_sand)
                14->coordinatorlayout.setBackgroundResource(R.drawable.main_bk_snow)
                15->coordinatorlayout.setBackgroundResource(R.drawable.mainrainy)
            }
        }
        //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        var checkdatabase=pers.getInt("checkdatabase",0)
        val flag1=pers.getString("name","").toString()=="????????????"
        val flag2=pers.getString("name","").toString()=="????????????"
        if ((pers.getInt("checkdatabase",0)<7)&&(flag1||flag2)){
            LogUtil.d("mainactivitytestdatabase","indatabase")
            var databaselength=0
            val lng1=pers.getString("lng","")
            val lat1=pers.getString("lat","")
            val dbHelper= SaveLocationDatabase(this,"LocationSave.db",1)
            val db=dbHelper.writableDatabase
            val cursor = db.query("Location", null, null, null, null, null, null)
            if (cursor.moveToFirst()) {
                do {
                   databaselength++
                } while (cursor.moveToNext())
            }
            cursor.close()
            if (databaselength==0){
                val insert1= ContentValues().apply {
                    put("AccurateLocation","????????????")
                    put("RoughLocation","????????????")
                    put("lat",lat1)
                    put("lng",lng1)
                    put("sourcetype",1)
                }
                db.insert("Location",null,insert1)
                dbHelper.close()
            }
            checkdatabase++
            val edit3=pers.edit()
            edit3.putInt("checkdatabase",checkdatabase)
            edit3.apply()
            dbHelper.close()
        }
    }

    private fun showfifteendays(hourly: Hourly){
        val layoutManager=LinearLayoutManager(this)
        layoutManager.orientation= RecyclerView.HORIZONTAL
        main_layout15WeatherListview.layoutManager=layoutManager
        val adapter= fifteenDayWeatherAdapter(hourly)
        main_layout15WeatherListview.adapter=adapter
    }
    private fun showthreedays(daily: Daily){
        val skycon1=daily.skycon[0].value
        val skycon2=daily.skycon[1].value
        val skycon3=daily.skycon[2].value
        val systemTime1: CharSequence = DateFormat.format("EEEE", System.currentTimeMillis())
        val systemTime=systemTime1.toString()
        //??????????????????????????????????????????
        when(skycon1){
            "CLEAR_DAY" -> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday101)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_clear_day)
            }
            "CLEAR_NIGHT" -> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday101)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_clear_night)
            }
            "PARTLY_CLOUDY_DAY" -> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday102)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_partly_cloudy_day)
            }
            "PARTLY_CLOUDY_NIGHT" -> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday102)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_partly_cloudy_night)
            }
            "CLOUDY"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday103)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_cloudy)
            }
            "LIGHT_HAZE"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday104)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_haze)
            }
            "MODERATE_HAZE"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday105)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_haze)
            }
            "HEAVY_HAZE"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday106)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_haze)
            }
            "LIGHT_RAIN"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday107)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_light_rain)
            }
            "MODERATE_RAIN"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday108)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_moderate_rain)
            }
            "HEAVY_RAIN"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday109)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_heavy_rain)
            }
            "STORM_RAIN"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday110)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_storm_rain)
            }
            "FOG"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday111)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_fog)
            }
            "LIGHT_SNOW"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday112)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_light_snow)
            }
            "MODERATE_SNOW"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday113)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_moderate_snow)
            }
            "HEAVY_SNOW"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday114)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_heavy_snow)
            }
            "STORM_SNOW"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday115)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_storm_snow)
            }
            "DUST"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday116)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_dust)
            }
            "SAND"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday117)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_sand)
            }
            "WIND"-> {
                coordinatorlayout.mainthreedayweatheritem1l.text=this.getString(R.string.mainthreeday118)
                coordinatorlayout.mainthreedayweathericon1.setImageResource(R.drawable.ic_weather_wind)
            }
        }
        when(skycon2){
            "CLEAR_DAY" -> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1010)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_clear_day)
            }
            "CLEAR_NIGHT" -> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1010)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_clear_night)
            }
            "PARTLY_CLOUDY_DAY" -> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1020)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_partly_cloudy_day)
            }
            "PARTLY_CLOUDY_NIGHT" -> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1020)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_partly_cloudy_night)
            }
            "CLOUDY"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1030)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_cloudy)
            }
            "LIGHT_HAZE"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1040)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_haze)
            }
            "MODERATE_HAZE"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1050)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_haze)
            }
            "HEAVY_HAZE"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1060)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_haze)
            }
            "LIGHT_RAIN"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1070)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_light_rain)
            }
            "MODERATE_RAIN"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1080)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_moderate_rain)
            }
            "HEAVY_RAIN"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1090)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_heavy_rain)
            }
            "STORM_RAIN"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1100)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_storm_rain)
            }
            "FOG"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1110)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_fog)
            }
            "LIGHT_SNOW"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1120)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_light_snow)
            }
            "MODERATE_SNOW"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1130)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_moderate_snow)
            }
            "HEAVY_SNOW"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1140)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_heavy_snow)
            }
            "STORM_SNOW"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1150)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_storm_snow)
            }
            "DUST"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1160)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_dust)
            }
            "SAND"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1170)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_sand)
            }
            "WIND"-> {
                coordinatorlayout.mainthreedayweatheritem2l.text=this.getString(R.string.mainthreeday1180)
                coordinatorlayout.mainthreedayweathericon2l.setImageResource(R.drawable.ic_weather_wind)
            }
        }
        when(skycon3) {
            "CLEAR_DAY" -> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1011)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_clear_day)
            }
            "CLEAR_NIGHT" -> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1011)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_clear_night)
            }
            "PARTLY_CLOUDY_DAY" -> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime +this.getString(R.string.mainthreeday1021)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_partly_cloudy_day)
            }
            "PARTLY_CLOUDY_NIGHT" -> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1021)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_partly_cloudy_night)
            }
            "CLOUDY"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1031)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_cloudy)
            }
            "LIGHT_HAZE"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1041)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_haze)
            }
            "MODERATE_HAZE"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1051)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_haze)
            }
            "HEAVY_HAZE"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1061)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_haze)
            }
            "LIGHT_RAIN"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1071)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_light_rain)
            }
            "MODERATE_RAIN"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1081)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_moderate_rain)
            }
            "HEAVY_RAIN"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1091)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_heavy_rain)
            }
            "STORM_RAIN"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1101)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_storm_rain)
            }
            "FOG"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1111)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_fog)
            }
            "LIGHT_SNOW"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1121)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_light_snow)
            }
            "MODERATE_SNOW"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1131)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_moderate_snow)
            }
            "HEAVY_SNOW"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1141)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_heavy_snow)
            }
            "STORM_SNOW"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1151)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_storm_snow)
            }
            "DUST"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1161)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_dust)
            }
            "SAND"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1171)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_sand)
            }
            "WIND"-> {
                coordinatorlayout.mainthreedayweatheritem3l.text=systemTime+this.getString(R.string.mainthreeday1181)
                coordinatorlayout.mainthreedayweathericon3l.setImageResource(R.drawable.ic_weather_wind)
            }
        }
        //?????????????????????????????????????????????
        val airQuality=judgeAirQuality(daily.air_quality.aqi[0].avg.chn)
        val airQuality2=judgeAirQuality(daily.air_quality.aqi[1].avg.chn)
        val airQuality3=judgeAirQuality(daily.air_quality.aqi[2].avg.chn)
        mainthreedayweatheritem1m.text=airQuality
        mainthreedayweatheritem2m.text=airQuality2
        mainthreedayweatheritem3m.text=airQuality3
        //??????????????????????????????????????????
        val df = DecimalFormat("#")
        df.roundingMode=RoundingMode.CEILING
        val max1=df.format(daily.temperature[0].max).toString()
        val max2=df.format(daily.temperature[1].max).toString()
        val max3=df.format(daily.temperature[2].max).toString()
        val min1=df.format(daily.temperature[0].min).toString()
        val min2=df.format(daily.temperature[1].min).toString()
        val min3=df.format(daily.temperature[2].min).toString()
        mainthreedayweatheritem1r.text=max1+this.getString(R.string.mainthreedaytemperaturemaxandmin1)+min1+this.getString(R.string.mainthreedaytemperaturemaxandmin2)
        mainthreedayweatheritem2r.text=max2+this.getString(R.string.mainthreedaytemperaturemaxandmin1)+min2+this.getString(R.string.mainthreedaytemperaturemaxandmin2)
        mainthreedayweatheritem3r.text=max3+this.getString(R.string.mainthreedaytemperaturemaxandmin1)+min3+this.getString(R.string.mainthreedaytemperaturemaxandmin2)
    }
    private fun judgeAirQuality(Air:Double):String{
        when(Air){
            in 0.0..50.0 ->return " ??? "
            in 50.1..100.0 ->return " ??? "
            in 100.1..150.0 ->return " ???????????? "
            in 150.1..200.0 ->return " ???????????? "
            in 200.1..300.0 ->return " ???????????? "
            in 300.1..1000.0 ->return " ???????????? "
        }
        return ""
    }
    private fun showtop(temperature:Int,skycon:String){
        when(skycon) {
            "CLEAR_DAY" -> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1012)
            }
            "CLEAR_NIGHT" -> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1012)
            }
            "PARTLY_CLOUDY_DAY" -> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1022)
            }
            "PARTLY_CLOUDY_NIGHT" -> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1022)
            }
            "CLOUDY"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1032)
            }
            "LIGHT_HAZE"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1042)
            }
            "MODERATE_HAZE"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1052)
            }
            "HEAVY_HAZE"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1062)
            }
            "LIGHT_RAIN"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1072)
            }
            "MODERATE_RAIN"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1082)
            }
            "HEAVY_RAIN"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1092)
            }
            "STORM_RAIN"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1102)
            }
            "FOG"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1112)
            }
            "LIGHT_SNOW"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1122)
            }
            "MODERATE_SNOW"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1132)
            }
            "HEAVY_SNOW"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1142)
            }
            "STORM_SNOW"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1152)
            }
            "DUST"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1162)
            }
            "SAND"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1172)
            }
            "WIND"-> {
                Main_TemperatureType.text=this.getString(R.string.mainthreeday1182)
            }
        }
        Main_Temperature.text=temperature.toString()
    }
    private fun showearlyWarningInformation(alert:Alert){
        LogUtil.d("clicktest1",alert.content.size.toString())
       when(alert.content.size){
            0-> {
                main_threedayweatheritemgap.visibility=View.GONE
                main_threedayweatheritemgap2.visibility=View.GONE
                main_threedayweatheritemgap3.visibility=View.VISIBLE
                earlyWarningInformation1.visibility = View.GONE
                earlyWarningInformation2.visibility = View.GONE
            }
           1->{
               main_threedayweatheritemgap3.visibility=View.GONE
               main_threedayweatheritemgap.visibility=View.VISIBLE
               main_threedayweatheritemgap2.visibility=View.GONE
               earlyWarningInformation1.visibility = View.VISIBLE
               earlyWarningInformation2.visibility = View.GONE
               LogUtil.d("mainactivitytest",alert.content.toString())
               main_earlywarningb1.text=alert.content[0].description
               main_earlywarningt1.text=alert.content[0].title
           }
           2,3,4,5,6,7->{
               main_threedayweatheritemgap.visibility=View.GONE
               main_threedayweatheritemgap3.visibility=View.GONE
               main_threedayweatheritemgap2.visibility=View.VISIBLE
               earlyWarningInformation1.visibility = View.VISIBLE
               earlyWarningInformation2.visibility = View.VISIBLE

               main_earlywarningb1.text=alert.content[0].description
               main_earlywarningt1.text=alert.content[0].title
               main_earlywarningb2.text=alert.content[1].description
               main_earlywarningt2.text=alert.content[1].title
           }

        }
        earlyWarningInformationlayout.setOnClickListener {
            LogUtil.d("clicktest","testok")
        }
    }
    private fun showairandultraviolet(realtime:Realtime){
        val number2=realtime.air_quality.aqi.chn.toInt().toString()
        LogUtil.d("activitymain1",number2.toString())
        if(realtime.air_quality.description.chn.equals("???")){
            main_airultravioletl.text=this.getString(R.string.main_airquiltyandultraviolet3)+number2
        }else if(realtime.air_quality.description.chn.equals("???")){
            main_airultravioletl.text=this.getString(R.string.main_airquiltyandultraviolet4)+number2
        }else{
            main_airultravioletl.text=realtime.air_quality.description.chn+number2
        }
        val number=realtime.life_index.ultraviolet.index
        if (number.toInt()==0){
            main_airultravioletr.text=this.getString(R.string.main_airquiltyandultraviolet1)
        }else{
            main_airultravioletr.text=this.getString(R.string.main_airquiltyandultraviolet2)+realtime.life_index.ultraviolet.desc+" "+number.toInt().toString()
        }
    }
    private fun showsunriseandset(astro: Astro,wind: Wind,humidity:Double,apparenttemperature:Double,pressure:Double){
        main_sunandrise19.text=astro.sunrise.time
        main_sunandrise20.text=astro.sunset.time
        when(wind.direction){
            in 22.6..67.5->main_sunandrise21.text=this.getString(R.string.direction11)
            in 67.6..112.5->main_sunandrise21.text=this.getString(R.string.direction12)
            in 112.6..157.5->main_sunandrise21.text=this.getString(R.string.direction13)
            in 157.6..202.5->main_sunandrise21.text=this.getString(R.string.direction14)
            in 202.6..247.5->main_sunandrise21.text=this.getString(R.string.direction15)
            in 247.6..292.5->main_sunandrise21.text=this.getString(R.string.direction16)
            in 292.6..337.5->main_sunandrise21.text=this.getString(R.string.direction17)
            in 337.6..361.0->main_sunandrise21.text=this.getString(R.string.direction18)
            in 0.0..22.5->main_sunandrise21.text=this.getString(R.string.direction18)
        }
        when(wind.speed){
            in 0.0..1.0 -> main_sunandrise22.text=this.getString(R.string.aa???)
            in 1.1..5.5 ->main_sunandrise22.text=this.getString(R.string.ab???)
            in 5.6..11.5 ->main_sunandrise22.text=this.getString(R.string.ac???)
            in 11.6..19.5 ->main_sunandrise22.text=this.getString(R.string.ad???)
            in 19.6..28.5 ->main_sunandrise22.text=this.getString(R.string.ae???)
            in 28.6..38.5 ->main_sunandrise22.text=this.getString(R.string.af???)
            in 38.6..49.5 ->main_sunandrise22.text=this.getString(R.string.ag???)
            in 49.6..61.5 ->main_sunandrise22.text=this.getString(R.string.ah???)
            in 61.6..74.5 ->main_sunandrise22.text=this.getString(R.string.ai???)
            in 74.6..88.5 ->main_sunandrise22.text=this.getString(R.string.aj???)
            in 88.6..102.5 ->main_sunandrise22.text=this.getString(R.string.ak???)
            in 102.6..117.5 ->main_sunandrise22.text=this.getString(R.string.al???)
            in 117.5..148.5 ->main_sunandrise22.text=this.getString(R.string.am???)
            in 148.6..500.1 ->main_sunandrise22.text=this.getString(R.string.an???)
        }
        main_sunandrise24.text=(humidity*100).toInt().toString()+this.getString(R.string.baifenbi)
        main_sunandrise26.text=apparenttemperature.toString()+this.getString(R.string.maintoptemperature2)
        main_sunandrise28.text=(pressure/100).toInt().toString()+this.getString(R.string.main_sunriseandset4)
    }
    private fun showlifeindex(precipitation_2h:ArrayList<Double>,comfortdesc:CommonUse3,dressdesc:CommonUse3,coldrisk:String,temperature:Int,skycon: String,washcar:String){
        //??????????????????
        var flag=false
        for(index in precipitation_2h){
            if (index!=0.0){
                flag=true
                break
            }
        }
        if(flag){
            lifeindexitem11.text=this.getString(R.string.main_lifeindex2)
            lifeindexitem12.setImageResource(R.drawable.ic_main_lifeindex_umbrella)
        }else{
            lifeindexitem11.text=this.getString(R.string.main_lifeindex1)
            lifeindexitem12.setImageResource(R.drawable.ic_main_lifeindex_noumbrella)
        }
        //?????????
        if (comfortdesc.index == "11"){
            lifeindexitem31.text=this.getString(R.string.main_lifeindex3)
        }else{
            lifeindexitem31.text=this.getString(R.string.main_lifeindex4)+comfortdesc.desc
        }
        when(comfortdesc.index){
            "0","1","2","3"->lifeindexitem32.setImageResource(R.drawable.ic_main_lifeindex_comfort_hot)
            "4","5","6"->lifeindexitem32.setImageResource(R.drawable.ic_main_lifeindex_comfort_comfort)
            "7","8","9","10","11","12","13"->lifeindexitem32.setImageResource(R.drawable.ic_main_lifeindex_comfort_cold)
        }
        //??????
        when(dressdesc.index){
            "0","1","2","3"->lifeindexitem22.setImageResource(R.drawable.ic_main_lifeindex_dress_hot)
            "4","5","6"->lifeindexitem22.setImageResource(R.drawable.ic_main_lifeindex_dress_comfort)
            "7","8"->lifeindexitem22.setImageResource(R.drawable.ic_main_lifeindex_dress_cold)
        }
        lifeindexitem21.text=this.getString(R.string.main_lifeindex5)+dressdesc.desc
        //??????
        lifeindexitem41.text=coldrisk+this.getString(R.string.main_lifeindex9)
        //??????
        if((skycon=="CLEAR_DAY"&&temperature>18)||(skycon=="CLEAR_NIGHT"&&temperature>18)){
            lifeindexitem51.text=this.getString(R.string.main_lifeindex6)
        }else{
            lifeindexitem51.text=this.getString(R.string.main_lifeindex7)
        }
        //??????
        lifeindexitem61.text=washcar+this.getString(R.string.main_lifeindex8)
    }
}
