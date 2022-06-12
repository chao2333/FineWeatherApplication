package com.fineweather.android.ui

import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.fineweather.android.R
import java.util.jar.Manifest

class MainUltravioletActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ultraviolet)


    }
}
fun main(){
    for(index in 1..10){
        val test=(0..10).random()
        println(test)
    }
}