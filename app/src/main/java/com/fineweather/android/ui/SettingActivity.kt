package com.fineweather.android.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fineweather.android.R
import com.fineweather.android.ui.setting.SettingAboutActivity
import com.fineweather.android.ui.setting.SettingPrivacypolicyActivity
import com.fineweather.android.ui.setting.SettingThemeActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        backMainActivity78.setOnClickListener {
            finish()
        }
        Privacy_policy.setOnClickListener {
            val intent=Intent(this,SettingPrivacypolicyActivity::class.java)
            startActivity(intent)
        }
        Privacy_policy1.setOnClickListener {
            val intent=Intent(this,SettingPrivacypolicyActivity::class.java)
            startActivity(intent)
        }
        setting_theme.setOnClickListener {
            val intent=Intent(this, SettingThemeActivity::class.java)
            startActivity(intent)
        }
        setting_theme1.setOnClickListener {
            val intent=Intent(this, SettingThemeActivity::class.java)
            startActivity(intent)
        }
        setting_about.setOnClickListener {
            val intent=Intent(this,SettingAboutActivity::class.java)
            startActivity(intent)
        }
        setting_about1.setOnClickListener {
            val intent=Intent(this,SettingAboutActivity::class.java)
            startActivity(intent)
        }
        //设置MPAndroidChart链接
        setting_transfer3.setOnClickListener {
            val intent1 = Intent(Intent.ACTION_VIEW)
            intent1.data = Uri.parse("https://github.com/PhilJay/MPAndroidChart")
            startActivity(intent1)
        }
        setting_transfer2.setOnClickListener {
            val intent3 = Intent(Intent.ACTION_VIEW)
            intent3.data = Uri.parse("http://www.caiyunapp.com/")
            startActivity(intent3)
        }
    }
}