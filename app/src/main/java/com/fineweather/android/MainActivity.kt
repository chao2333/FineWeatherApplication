package com.fineweather.android

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fineweather.android.ui.WelcomeActivity
import com.fineweather.android.ui.location.LocationActivity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.R.anim.abc_fade_in as animAbc_fade_in

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        topTextView.setText("新郑市 文昌路")
        //实现欢迎界面
        Welcome()
        topSettingButton.setOnClickListener{
            coordinatorlayout.setBackgroundResource(R.drawable.mainrainy)
            val LocationIntent3= Intent(this, WelcomeActivity::class.java)
            startActivity(LocationIntent3)
        }
        topLocationButton.setOnClickListener{
            coordinatorlayout.setBackgroundResource(R.drawable.mainsunny)
            val LocationIntent= Intent(this, LocationActivity::class.java)
            startActivity(LocationIntent)
        }

    }
    fun Welcome(){
        val ApplicationDataPers=getSharedPreferences("ApplicationData",0)
        val editor=ApplicationDataPers.edit()
        val Firstload=ApplicationDataPers.getBoolean("Firstload",false)
        editor.apply()
        if(!Firstload){
            val WelcomeIntent=Intent(this,WelcomeActivity::class.java)
            startActivity(WelcomeIntent)
        }
        editor.putBoolean("Firstload",true)
        editor.apply()
    }
}
