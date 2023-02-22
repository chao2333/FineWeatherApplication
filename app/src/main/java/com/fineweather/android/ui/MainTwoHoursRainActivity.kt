package com.fineweather.android.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.fineweather.android.R
import com.fineweather.android.logic.dao.LogUtil
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_main_air_quality.*
import kotlinx.android.synthetic.main.activity_main_two_hours_rain.*
import kotlin.math.max

class MainTwoHoursRainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_two_hours_rain)
        //进行屏幕适配
        CustomDensityUtil.setCustomDensity(this,application)
        backMainActivity10.setOnClickListener {
            finish()
        }
        val twohoursrain=intent.getSerializableExtra("twohoursraindata") as ArrayList<Double>
        LogUtil.d("maintwohoursrainactivitytest",twohoursrain.toString())
        /*val test= arrayListOf(0.0)
        for (index in 1..119){
            test.add(index.toDouble())
        }*/
        setchart(twohoursrain,this)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun setchart(twohoursrain: ArrayList<Double>, activity: MainTwoHoursRainActivity){

        //获取表格实例
        val chart=twohoursrain_chart
        //初始化表格并设置数据
        val values:ArrayList<BarEntry> = arrayListOf(BarEntry(1f,twohoursrain[0].toFloat(),"sada"))
        //获取Y轴实例
        val yAxis=chart.axisLeft
        for(index in 1..119){
            values.add(BarEntry(index.toFloat(),twohoursrain[index].toFloat()))
        }
        //自定义x轴
        val xAxis = chart.xAxis
        val timeArray= arrayListOf("","20分钟后","","一小时后","","一小时40分钟后")
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(index: Float, axisBase: AxisBase): String? {
                return timeArray[(index/20).toInt()]
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
        for (i in twohoursrain){
            currentMax= max(currentMax,i)
            if (i>0.03&&i<=0.25) color.add(activity.getColor(R.color.bkSunnyEnd))
            if (i>0.25&&i<=0.35) color.add(activity.getColor(R.color.rianFall_Yellow))
            if (i>0.35&&i<=0.48) color.add(activity.getColor(R.color.rianFall_Orange))
            if (i>0.48) color.add(activity.getColor(R.color.rianFall_Red))
        }

        set1.setColors(color)
        set1.isHighlightEnabled=false//设置选中不高亮显示

        //设置表格最大值
        if (currentMax<0.65){
            yAxis.axisMaximum= 0.65F
        }else{
            yAxis.axisMaximum=currentMax.toFloat()
        }
        val data1= BarData(set1)
        chart.setScaleEnabled(false)//设置Y轴不能缩放
        chart.axisRight.isEnabled=false
        chart.data=data1
    }
}