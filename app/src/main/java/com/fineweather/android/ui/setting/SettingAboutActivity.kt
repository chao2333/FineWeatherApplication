package com.fineweather.android.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.R
import com.fineweather.android.logic.dao.LogUtil
import kotlinx.android.synthetic.main.activity_setting_about.*
import java.net.HttpURLConnection
import java.net.NetworkInterface
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class SettingAboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_about)
        backMainActivity80.setOnClickListener {
            finish()
        }
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
        setting_about_ic.setOnClickListener {
            val mac=applicationContext.getSystemService(Context.WIFI_SERVICE)
           /*
            val date = Date(System.currentTimeMillis())
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            LogUtil.d("networktest2",System.currentTimeMillis().toString()+NetworkInterface.getNetworkInterfaces().toString())
            LogUtil.d("networktest2","in test")
           thread {
               val response = StringBuilder()
               val url= URL("http://fineweatherapp.cooc.site:8080/test.jsp?date="+System.currentTimeMillis().toString()+"&mac="+NetworkInterface.getNetworkInterfaces().toString())
               url.openConnection() as HttpURLConnection
            }
            */
        }
    }
}