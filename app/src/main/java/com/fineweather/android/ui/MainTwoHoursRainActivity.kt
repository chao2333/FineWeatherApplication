package com.fineweather.android.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_main_two_hours_rain.*

class MainTwoHoursRainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_two_hours_rain)
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
    fun setchart(twohoursrain: ArrayList<Double>, activity: MainTwoHoursRainActivity){
        //获取表格实例
        val chart=twohoursrain_chart
        //初始化表格并设置数据
        val values:ArrayList<BarEntry> = arrayListOf(BarEntry(1f,twohoursrain[0].toFloat(),"sada"))
        //获取X轴实例
        val xl=chart.xAxis
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
    for(index in 1..24){
      println(index)
    }
}