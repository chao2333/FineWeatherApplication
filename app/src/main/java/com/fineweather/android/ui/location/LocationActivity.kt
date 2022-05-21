package com.fineweather.android.ui.location

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityOptionsCompat
import com.fineweather.android.R
import kotlinx.android.synthetic.main.activity_location.*

class LocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        backMainActivity.setOnClickListener {
            finish()
        }
        searchPlaceButton.setOnClickListener {
            val intentToSearch=Intent(this,LocationSearchActivity::class.java)
            val options=ActivityOptionsCompat.makeSceneTransitionAnimation(this,searchPlaceButton,"SearchTransitionAnimation")
            startActivity(intentToSearch,options.toBundle())
            Log.d("mytest","inbuttonclick")
        }
    }
}