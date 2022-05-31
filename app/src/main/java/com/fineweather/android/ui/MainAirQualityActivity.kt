package com.fineweather.android.ui

import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_main_air_quality.*
import java.text.DecimalFormat


class MainAirQualityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_air_quality)
        val Airqualitydata=intent.getSerializableExtra("Airqualitydata") as Airqualitydata
        LogUtil.d("mainairqualitytest",Airqualitydata.timearraylist.toString())
        LogUtil.d("mainairqualitytest",Airqualitydata.aqichnlist.toString())
        var values:ArrayList<BarEntry> = arrayListOf(BarEntry(10f,10.0f,"sada"))
        val x1=airquality_chart.xAxis
        for(index in 0..23){

            values.add(BarEntry(index.toFloat(),Airqualitydata.aqichnlist[index].toFloat()))

        }
       // val formatter = ValueFormatter()
        val xl=airquality_chart.xAxis
    //    xl.setValueFormatter(formatter)
        xl.setPosition(XAxis.XAxisPosition.BOTTOM)
       val set1 = BarDataSet(values,"")
        val data1= BarData(set1)

        airquality_chart.axisRight.isEnabled=false
        airquality_chart.data=data1


    }
}


fun main(){
    val test="2022-06-05T00:00+08:00"
    val test2=test.split("T",":")[1]
    print(test2[1])
}
public class DecimalFormatter:IAxisValueFormatter{
    private lateinit var format: DecimalFormat

    public fun DecimalFormatter(){
        format= DecimalFormat("###,###,##0.00")
    }
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return  format.format(value)+"$"
    }

}

class ValueFormatter : IAxisValueFormatter {
    private val xStrs = arrayOf("春", "夏", "秋", "冬")
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        var position = value.toInt()
        if (position >= 4) {
            position = 0
        }
        return xStrs[position]
    }
}