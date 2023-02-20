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
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_main_air_quality.*
import java.lang.reflect.Array
import java.text.DecimalFormat


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
    fun setchart(Airqualitydata:Airqualitydata,activity: MainAirQualityActivity){
        //获取表格实例
        val chart=airquality_chart
        //初始化表格并设置数据
        val values:ArrayList<BarEntry> = arrayListOf(BarEntry(1f,Airqualitydata.aqichnlist[0].toFloat(),"sada"))
        //获取X轴实例
        val xl=chart.xAxis
        for(index in 1..24){
            values.add(BarEntry(index.toFloat(),Airqualitydata.aqichnlist[index].toFloat()))
        }
        //自定义x轴
        val xAxis = chart.xAxis
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
        xl.setPosition(XAxis.XAxisPosition.BOTTOM)//设置X轴线显示在表格底部
        xl.setDrawGridLines(false)//设置不显示竖轴线
        chart.description.text=""//设置表格右下角描述
        val set1 = BarDataSet(values,"")
        set1.isHighlightEnabled=false//设置选中不高亮显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            set1.setColor(activity.getColor(R.color.bkSunnyEnd))
        }
        val data1= BarData(set1)
        chart.axisRight.isEnabled=false
        chart.data=data1


    }
}


fun main(){
    val test="2022-06-05T12:00+08:00"
    val timeArray= test.split("T","+")[1]
    print(timeArray)

}

