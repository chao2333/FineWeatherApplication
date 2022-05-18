package com.fineweather.android

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        topTextView.setText("新郑市 文昌路")
        topSettingButton.setOnClickListener{
            coordinatorlayout.setBackgroundResource(R.drawable.mainrainy)
        }
        topLocationButton.setOnClickListener{
            coordinatorlayout.setBackgroundResource(R.drawable.mainsunny)
        }

    }
}