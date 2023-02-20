package com.fineweather.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fineweather.android.R
import com.fineweather.android.logic.model.Daily
import kotlinx.android.synthetic.main.activity_main_fifteen_days.*


class MainFifteenDaysActivity : AppCompatActivity() {
    private lateinit var adapter: FifteenDayLayoutAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fifteen_days)
        //进行屏幕适配
        CustomDensityUtil.setCustomDensity(this,application)
        val Daily=intent.getSerializableExtra("dailydata") as Daily
        val layoutManager=LinearLayoutManager(this)
        layoutManager.orientation= RecyclerView.HORIZONTAL
        fifteenday_recyclerlist.layoutManager=layoutManager
        adapter=FifteenDayLayoutAdapter(Daily,this)
        fifteenday_recyclerlist.adapter=adapter
        backMainActivity6.setOnClickListener {
            finish()
        }
    }

}