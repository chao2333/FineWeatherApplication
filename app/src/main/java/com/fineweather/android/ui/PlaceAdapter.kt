package com.fineweather.android.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.Drawable
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.fineweather.android.FineWeatherApplication
import com.fineweather.android.R
import com.fineweather.android.logic.dao.LogUtil
import com.fineweather.android.logic.dao.SaveLocationDatabase
import com.fineweather.android.logic.model.LocationSaveItem
import com.fineweather.android.logic.model.Place
import kotlinx.android.synthetic.main.locationsave_item.view.*

class PlaceAdapter(private val activity:Activity,private val placeList:List<Place>):
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val placeName:TextView=view.findViewById(R.id.placeName)
        val placeAddress:TextView=view.findViewById(R.id.placeAddress)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LogUtil.d("onviewholder","privrous viewholder has creat")
        val view= LayoutInflater.from(parent.context).inflate(R.layout.locationsearch_item,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val place=placeList[position]
        holder.placeName.text=place.name
        holder.placeAddress.text=place.address
        //为每一个卡片设置点击事件
        holder.itemView.setOnClickListener {
            val position=holder.bindingAdapterPosition
            val location=placeList[position].location
            val dbHelper=SaveLocationDatabase(activity,"LocationSave.db",1)
            val db=dbHelper.writableDatabase
            val insert1=ContentValues().apply {
                put("AccurateLocation",place.address)
                put("RoughLocation",place.name)
                put("lat",place.location.lat)
                put("lng",place.location.lng)
                activity.onBackPressed()
            }
            db.insert("Location",null,insert1)
            Toast.makeText(FineWeatherApplication.context,"已经添加到我的城市",Toast.LENGTH_LONG).show()
            dbHelper.close()
        }
    }
    override fun getItemCount()=placeList.size
}


class LocationSaveAdapter(private val activity:Activity,private val savelist: ArrayList<LocationSaveItem>):
    RecyclerView.Adapter<LocationSaveAdapter.LocationSaveHolder>(){
    inner class LocationSaveHolder(View:View):RecyclerView.ViewHolder(View){
        val locationSaveCardRough:TextView=View.findViewById(R.id.LocationSaveCardRough)
        val locationSaveCardAccu:TextView=View.findViewById(R.id.LocationSaveCardAccu)
        val locationSaveImageView:ImageView=View.findViewById(R.id.LocationSaveCardImageview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationSaveHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.locationsave_item,parent,false)
        return LocationSaveHolder(view)
    }

    @SuppressLint("Range", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: LocationSaveHolder, position: Int) {
        val position2=holder.layoutPosition
        val location=savelist[position2]
        holder.locationSaveCardAccu.text=location.address
        holder.locationSaveCardRough.text=location.name
        holder.itemView.setOnLongClickListener {
            holder.locationSaveImageView.visibility=View.VISIBLE
            false
        }
        holder.itemView.setOnClickListener {
            //设置卡片背景
            // it.LocationSaveCardbk.setBackgroundResource(R.drawable.mainsunny)

            val pers=FineWeatherApplication.context.getSharedPreferences("ApplicationData",0)
            val persedit=pers.edit()
            persedit.putString("name",location.name)
            persedit.putString("address",location.address)
            persedit.putString("lat",location.lat)
            persedit.putString("lng",location.lng)
            //来源为搜索，false   来源为定位为true
            persedit.putBoolean("sourcetype",false)
            persedit.apply()
            activity.finish()
            Toast.makeText(activity,"切换地点为${location.name}",Toast.LENGTH_LONG).show()
        }
        //删除当前item
        holder.locationSaveImageView.setOnClickListener {
            val dbHelperWrite=SaveLocationDatabase(activity,"LocationSave.db",1).writableDatabase
            dbHelperWrite.execSQL("DELETE FROM Location WHERE RoughLocation='${location.name}'")
            savelist.remove(LocationSaveItem(location.name,location.address,location.lat,location.lng))
            notifyItemRemoved(position2)
            notifyDataSetChanged()
            holder.locationSaveImageView.visibility=GONE
        }
    }
    override fun getItemCount(): Int {
        return savelist.size
    }

}






























