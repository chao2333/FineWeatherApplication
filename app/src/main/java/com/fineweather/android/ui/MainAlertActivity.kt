package com.fineweather.android.ui

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.fineweather.android.R
import com.fineweather.android.logic.model.Alert
import kotlinx.android.synthetic.main.activity_main_alert.*

class MainAlertActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_alert)
        val alertdata=intent.getSerializableExtra("alertdata") as Alert
        if (alertdata.content.size==1){
            alertlayout_layout1.visibility=View.VISIBLE
            alertlayout_layout2.visibility=View.GONE
            val code1=alertdata.content[0].code
            if (code1.length==4){
                var string1=""
                var string2=""
                val string3=this.getString(R.string.alert_layout)
                val m=(code1[0].toString()+code1[1].toString()).toInt()
                val n=(code1[2].toString()+code1[3].toString()).toInt()
                when(m){
                    1->string1=this.getString(R.string.alert_layout_code1)
                    2->string1=this.getString(R.string.alert_layout_code2)
                    3->string1=this.getString(R.string.alert_layout_code3)
                    4->string1=this.getString(R.string.alert_layout_code4)
                    5->string1=this.getString(R.string.alert_layout_code5)
                    6->string1=this.getString(R.string.alert_layout_code6)
                    7->string1=this.getString(R.string.alert_layout_code7)
                    8->string1=this.getString(R.string.alert_layout_code8)
                    9->string1=this.getString(R.string.alert_layout_code9)
                    10->string1=this.getString(R.string.alert_layout_code10)
                    11->string1=this.getString(R.string.alert_layout_code11)
                    12->string1=this.getString(R.string.alert_layout_code12)
                    13->string1=this.getString(R.string.alert_layout_code13)
                    14->string1=this.getString(R.string.alert_layout_code14)
                    15->string1=this.getString(R.string.alert_layout_code15)
                    16->string1=this.getString(R.string.alert_layout_code16)
                    17->string1=this.getString(R.string.alert_layout_code17)
                    18->string1=this.getString(R.string.alert_layout_code18)
                }
                when(n){
                    0->string2=this.getString(R.string.alert_layout_color0)
                    1-> {
                        string2 = this.getString(R.string.alert_layout_color1)
                        alertlayout_title1.setTextColor(this.resources.getColor(R.color.alert_layout1))
                        alertlayout_layout1.background=ResourcesCompat.getDrawable(resources,R.drawable.alert_layout_bk1,null)
                    }
                    2-> {
                        string2 = this.getString(R.string.alert_layout_color2)
                        alertlayout_title1.setTextColor(this.resources.getColor(R.color.alert_layout2))
                        alertlayout_layout1.background=ResourcesCompat.getDrawable(resources,R.drawable.alert_layout_bk2,null)
                    }
                    3-> {
                        string2 = this.getString(R.string.alert_layout_color3)
                        alertlayout_title1.setTextColor(this.resources.getColor(R.color.alert_layout3))
                        alertlayout_layout1.background=ResourcesCompat.getDrawable(resources,R.drawable.alert_layout_bk3,null)
                    }
                    4-> {
                        string2 = this.getString(R.string.alert_layout_color4)
                        alertlayout_title1.setTextColor(this.resources.getColor(R.color.alert_layout4))
                        alertlayout_layout1.background=ResourcesCompat.getDrawable(resources,R.drawable.alert_layout_bk4,null)
                    }
                }
                val string4=string1+string2+string3
                alertlayout_title1.text=string4
                alertlayout_detail1.text=alertdata.content[0].description
            }

        }else{
            alertlayout_layout1.visibility=View.VISIBLE
            alertlayout_layout2.visibility=View.VISIBLE
            val code1=alertdata.content[0].code
            if (code1.length==4){
                var string1=""
                var string2=""
                val string3=this.getString(R.string.alert_layout)
                val m=(code1[0].toString()+code1[1].toString()).toInt()
                val n=(code1[2].toString()+code1[3].toString()).toInt()
                when(m){
                    1->string1=this.getString(R.string.alert_layout_code1)
                    2->string1=this.getString(R.string.alert_layout_code2)
                    3->string1=this.getString(R.string.alert_layout_code3)
                    4->string1=this.getString(R.string.alert_layout_code4)
                    5->string1=this.getString(R.string.alert_layout_code5)
                    6->string1=this.getString(R.string.alert_layout_code6)
                    7->string1=this.getString(R.string.alert_layout_code7)
                    8->string1=this.getString(R.string.alert_layout_code8)
                    9->string1=this.getString(R.string.alert_layout_code9)
                    10->string1=this.getString(R.string.alert_layout_code10)
                    11->string1=this.getString(R.string.alert_layout_code11)
                    12->string1=this.getString(R.string.alert_layout_code12)
                    13->string1=this.getString(R.string.alert_layout_code13)
                    14->string1=this.getString(R.string.alert_layout_code14)
                    15->string1=this.getString(R.string.alert_layout_code15)
                    16->string1=this.getString(R.string.alert_layout_code16)
                    17->string1=this.getString(R.string.alert_layout_code17)
                    18->string1=this.getString(R.string.alert_layout_code18)
                }
                when(n){
                    0->string2=this.getString(R.string.alert_layout_color0)
                    1-> {
                        string2 = this.getString(R.string.alert_layout_color1)
                        alertlayout_title1.setTextColor(this.resources.getColor(R.color.alert_layout1))
                        alertlayout_layout1.background=ResourcesCompat.getDrawable(resources,R.drawable.alert_layout_bk1,null)
                    }
                    2-> {
                        string2 = this.getString(R.string.alert_layout_color2)
                        alertlayout_title1.setTextColor(this.resources.getColor(R.color.alert_layout2))
                        alertlayout_layout1.background=ResourcesCompat.getDrawable(resources,R.drawable.alert_layout_bk2,null)
                    }
                    3-> {
                        string2 = this.getString(R.string.alert_layout_color3)
                        alertlayout_title1.setTextColor(this.resources.getColor(R.color.alert_layout3))
                        alertlayout_layout1.background=ResourcesCompat.getDrawable(resources,R.drawable.alert_layout_bk3,null)
                    }
                    4-> {
                        string2 = this.getString(R.string.alert_layout_color4)
                        alertlayout_title1.setTextColor(this.resources.getColor(R.color.alert_layout4))
                        alertlayout_layout1.background=ResourcesCompat.getDrawable(resources,R.drawable.alert_layout_bk4,null)
                    }
                }
                val string4=string1+string2+string3
                alertlayout_title1.text=string4
                alertlayout_detail1.text=alertdata.content[0].description
            }

            val code2=alertdata.content[1].code
            if (code2.length==4){
                var string1=""
                var string2=""
                val string3=this.getString(R.string.alert_layout)
                val m=(code2[0].toString()+code2[1].toString()).toInt()
                val n=(code2[2].toString()+code2[3].toString()).toInt()
                when(m){
                    1->string1=this.getString(R.string.alert_layout_code1)
                    2->string1=this.getString(R.string.alert_layout_code2)
                    3->string1=this.getString(R.string.alert_layout_code3)
                    4->string1=this.getString(R.string.alert_layout_code4)
                    5->string1=this.getString(R.string.alert_layout_code5)
                    6->string1=this.getString(R.string.alert_layout_code6)
                    7->string1=this.getString(R.string.alert_layout_code7)
                    8->string1=this.getString(R.string.alert_layout_code8)
                    9->string1=this.getString(R.string.alert_layout_code9)
                    10->string1=this.getString(R.string.alert_layout_code10)
                    11->string1=this.getString(R.string.alert_layout_code11)
                    12->string1=this.getString(R.string.alert_layout_code12)
                    13->string1=this.getString(R.string.alert_layout_code13)
                    14->string1=this.getString(R.string.alert_layout_code14)
                    15->string1=this.getString(R.string.alert_layout_code15)
                    16->string1=this.getString(R.string.alert_layout_code16)
                    17->string1=this.getString(R.string.alert_layout_code17)
                    18->string1=this.getString(R.string.alert_layout_code18)
                }
                when(n){
                    0->string2=this.getString(R.string.alert_layout_color0)
                    1-> {
                        string2 = this.getString(R.string.alert_layout_color1)
                        alertlayout_title2.setTextColor(this.resources.getColor(R.color.alert_layout1))
                        alertlayout_layout2.background=ResourcesCompat.getDrawable(resources,R.drawable.alert_layout_bk1,null)
                    }
                    2-> {
                        string2 = this.getString(R.string.alert_layout_color2)
                        alertlayout_title2.setTextColor(this.resources.getColor(R.color.alert_layout2))
                        alertlayout_layout2.background=ResourcesCompat.getDrawable(resources,R.drawable.alert_layout_bk2,null)
                    }
                    3-> {
                        string2 = this.getString(R.string.alert_layout_color3)
                        alertlayout_title2.setTextColor(this.resources.getColor(R.color.alert_layout3))
                        alertlayout_layout2.background=ResourcesCompat.getDrawable(resources,R.drawable.alert_layout_bk3,null)
                    }
                    4-> {
                        string2 = this.getString(R.string.alert_layout_color4)
                        alertlayout_title2.setTextColor(this.resources.getColor(R.color.alert_layout4))
                        alertlayout_layout2.background=ResourcesCompat.getDrawable(resources,R.drawable.alert_layout_bk4,null)
                    }
                }
                val string4=string1+string2+string3
                alertlayout_title2.text=string4
                alertlayout_detail2.text=alertdata.content[1].description
            }
        }
        backMainActivity12.setOnClickListener {
            finish()
        }
    }
}
fun main(){
    val string="0006"
    val m=(string[0].toString()+string[1].toString()).toInt()
    val n=string[2].toString()+string[3].toString()
    println(m)
    println(n)

}