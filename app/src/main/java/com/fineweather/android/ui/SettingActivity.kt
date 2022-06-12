package com.fineweather.android.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fineweather.android.R
import com.fineweather.android.ui.setting.SettingPrivacypolicyActivity
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
    }
}