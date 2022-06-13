package com.fineweather.android.ui.setting

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fineweather.android.R
import kotlinx.android.synthetic.main.activity_setting_about.*

class SettingAboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_about)
        backMainActivity80.setOnClickListener {
            finish()
        }
      /*  setting_about_ic.setOnClickListener{
            val intent8 = Intent(Intent.ACTION_VIEW)
            intent8.data = Uri.parse("wxp://f2f0iAO4qxHAcR-CCGeGq87meax3atpo_Tl5WKmWXRIch4A")
            startActivity(intent8)
        }*/
        setting_about.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://wpa.qq.com/msgrd?v=3&uin=1552902376&site=qq&menu=yes")
            startActivity(intent)
        }
        setting_about1.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://wpa.qq.com/msgrd?v=3&uin=1552902376&site=qq&menu=yes")
            startActivity(intent)
        }
        setting_about_join.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3DMkPhjd3rF2og3YX504ydU-1P9tKoPX2z")
            startActivity(intent)
        }
    }
}