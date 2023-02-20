package com.fineweather.android.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fineweather.android.R
import com.fineweather.android.logic.Respository
import com.fineweather.android.ui.CustomDensityUtil
import kotlinx.android.synthetic.main.activity_setting_theme.*

class SettingThemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_theme)
        //进行屏幕适配
        CustomDensityUtil.setCustomDensity(this,application)
        val pers=Respository.getSqlite()

        //按照activity_setting_theme布局中主题的顺序，确定主题的编号
        setting_theme_gensuitianqi.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",0)
            edit.apply()
            Toast.makeText(this,"重启本软件生效",Toast.LENGTH_SHORT).show()
        }
        setting_theme_sunny.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",1)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_evening.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",2)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_sunny2.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",3)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_sunny3.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",4)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_sunnyafternoon.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",5)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_sunnyafternoon2.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",6)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_sunnymountain.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",7)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_sunnynocloud.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",8)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_sunnypeace.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",9)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_sunnypinkandblue.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",10)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_sunnysmall.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",11)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_night.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",12)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_sand.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",13)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_snow.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",14)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        setting_theme_rainy.setOnClickListener {
            val edit=pers.edit()
            edit.putInt("mainbk",15)
            edit.apply()
            Toast.makeText(this,"切换成功",Toast.LENGTH_SHORT).show()
        }
        backMainActivity79.setOnClickListener {
            finish()
        }
    }
}