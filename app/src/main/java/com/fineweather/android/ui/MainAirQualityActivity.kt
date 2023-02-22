package com.fineweather.android.ui

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.fineweather.android.R
import com.fineweather.android.logic.dao.LogUtil
import com.fineweather.android.logic.model.Airqualitydata
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_main_air_quality.*
import kotlin.math.max


class MainAirQualityActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_air_quality)
        //进行屏幕适配
        CustomDensityUtil.setCustomDensity(this,application)
        val Airqualitydata=intent.getSerializableExtra("Airqualitydata") as Airqualitydata
        setchart(Airqualitydata,this)
        //显示主界面数据
        Airquality_time.text=Airqualitydata.time.split("T","+")[1]+this.getString(R.string.qualitylayout_data1)//发布时间
        Airquality_airquality.text=Airqualitydata.airquality.toInt().toString()
        when(Airqualitydata.airquality){
            in 0.0..50.0-> {
                Airquality_desc.text=this.getString(R.string.a优)
                Airquality_airquality.setTextColor(this.getColor(R.color.Airquality_you))
                Airquality_desc.setTextColor(this.getColor(R.color.Airquality_you))
            }
            in 50.1..100.0-> {
                Airquality_desc.text=this.getString(R.string.a良)
                Airquality_airquality.setTextColor(this.getColor(R.color.Airquality_liang))
                Airquality_desc.setTextColor(this.getColor(R.color.Airquality_liang))
            }
            in 100.1..150.0-> {
                Airquality_desc.text=this.getString(R.string.a轻度污染)
                Airquality_airquality.setTextColor(this.getColor(R.color.Airquality_pollution1))
                Airquality_desc.setTextColor(this.getColor(R.color.Airquality_pollution1))
            }
            in 150.1..200.0-> {
                Airquality_desc.text=this.getString(R.string.a中度污染)
                Airquality_airquality.setTextColor(this.getColor(R.color.Airquality_pollution2))
                Airquality_desc.setTextColor(this.getColor(R.color.Airquality_pollution2))
            }
            in 200.1..600.0-> {
                Airquality_desc.text=this.getString(R.string.a重度污染)
                Airquality_airquality.setTextColor(this.getColor(R.color.Airquality_pollution3))
                Airquality_desc.setTextColor(this.getColor(R.color.Airquality_pollution3))
            }
        }//设置空气质量和描述字体颜色
        Airquality_pm25.text=Airqualitydata.pm25.toInt().toString()
        Airquality_pm10.text=Airqualitydata.pm10.toInt().toString()
        Airquality_SO2.text=Airqualitydata.so2.toInt().toString()
        Airquality_NO2.text=Airqualitydata.no2.toInt().toString()
        Airquality_O3.text=Airqualitydata.o3.toInt().toString()
        Airquality_CO.text=Airqualitydata.co.toString()
        backMainActivity1.setOnClickListener {
            finish()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun setchart(Airqualitydata:Airqualitydata, activity: MainAirQualityActivity){
        //获取表格实例
        val chart=airquality_chart
        //初始化表格并设置数据
        val values:ArrayList<BarEntry> = arrayListOf(BarEntry(1f,Airqualitydata.aqichnlist[0].toFloat(),"sada"))
        //获取X轴实例
        for(index in 1..24){
            values.add(BarEntry(index.toFloat(),Airqualitydata.aqichnlist[index].toFloat()))
        }
        //自定义x轴
        val xAxis = chart.xAxis
        val yAxis=chart.axisLeft
        val timeArray= (arrayOfNulls<String>(26))
        for(index in 0..23){
            val nosettime=Airqualitydata.timearraylist[index+1]
            val test1=nosettime.split("T",":")[1].toInt().toString()
            timeArray[index]= "$test1:00"
        }
        timeArray[24]=""
        timeArray[25]=""
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(index: Float, axisBase: AxisBase): String? {
                return timeArray[index.toInt()]
            }
        }
        //自定义x轴完成
        chart.legend.isEnabled=false
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)//设置X轴线显示在表格底部
        xAxis.setDrawGridLines(false)//设置不显示竖轴线
        chart.description.text=""//设置表格右下角描述

        val set1 = BarDataSet(values,"")
        //根据数据设置柱子颜色
        val color= mutableListOf<Int>()
        var currentMax=0.0
        val array=Airqualitydata.aqichnlist
        for (i in 1..24){
            currentMax= max(currentMax,array[i])
            if (array[i] in 0.0..50.0) color.add(activity.getColor(R.color.Airquality_you))
            else if (array[i]>50&&array[i]<=100) color.add(activity.getColor(R.color.Airquality_liang))
            else if (array[i]>100&&array[i]<=150) color.add(activity.getColor(R.color.Airquality_pollution1))
            else if (array[i]>150&&array[i]<=200) color.add(activity.getColor(R.color.Airquality_pollution2))
            else if (array[i]>200) color.add(activity.getColor(R.color.Airquality_pollution3))
        }
        set1.setColors(color)

        //设置柱子高度
        when(currentMax){
            in 0.0..25.0->yAxis.axisMaximum= 50.0F
            in 25.1..50.0->yAxis.axisMaximum= 80.0F
            in 50.1..75.0->yAxis.axisMaximum= 110.0F
            in 75.1..100.0->yAxis.axisMaximum= 130.0F
            else->yAxis.axisMaximum= (currentMax+currentMax/3).toFloat()
        }
        set1.isHighlightEnabled=false//设置选中不高亮显示

        chart.setScaleEnabled(false)//设置不能缩放
        chart.setMaxVisibleValueCount(0)//设置取消柱顶显示数据
        val data1= BarData(set1)
        chart.axisRight.isEnabled=false
        chart.data=data1


    }
}


