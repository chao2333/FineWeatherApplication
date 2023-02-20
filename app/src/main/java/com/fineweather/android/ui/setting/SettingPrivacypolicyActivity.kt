package com.fineweather.android.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fineweather.android.R
import com.fineweather.android.ui.CustomDensityUtil
import kotlinx.android.synthetic.main.activity_setting_privacypolicy.*

class SettingPrivacypolicyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_privacypolicy)
        //进行屏幕适配
        CustomDensityUtil.setCustomDensity(this,application)
        backSettingActivity.setOnClickListener {
            finish()
        }
    }
}