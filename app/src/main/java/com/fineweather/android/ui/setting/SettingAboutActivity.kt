package com.fineweather.android.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fineweather.android.R
import com.fineweather.android.ui.CustomDensityUtil
import kotlinx.android.synthetic.main.activity_setting_about.*

class SettingAboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_about)
        //进行屏幕适配
        CustomDensityUtil.setCustomDensity(this,application)
        backMainActivity80.setOnClickListener {
            finish()
        }
        setting_about3.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ycyc20@qq.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "说明来意")
            intent.putExtra(Intent.EXTRA_TEXT, "ycyc20@qq.com")

            startActivity(Intent.createChooser(intent, "ycyc20@qq.com"))
        }
        setting_about1.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ycyc20@qq.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "说明来意")
            intent.putExtra(Intent.EXTRA_TEXT, "ycyc20@qq.com")
            startActivity(Intent.createChooser(intent, "ycyc20@qq.com"))
        }
        setting_about_join.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3DMkPhjd3rF2og3YX504ydU-1P9tKoPX2z")
            startActivity(intent)
        }
    }
}