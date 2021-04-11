package com.sunnyweather.android.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.ui.weather.WeatherActivity

class PlaceAdapter (private val fragment:PlaceFragment,private val placeList:List<Place>):
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    companion object{
        const val LOCATION_LNG="location_lng"
        const val LOCATION_LAT="location_lat"
        const val PLACE_NAME="place_name"
    }
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val placeName:TextView=itemView.findViewById(R.id.placeName)
        val placeAddress:TextView=itemView.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        val viewHold=ViewHolder(view)
        viewHold.itemView.setOnClickListener {
            val place=placeList[viewHold.adapterPosition]
            val intent=Intent(parent.context,WeatherActivity::class.java).apply {
                putExtra(LOCATION_LAT,place.location.lat)
                putExtra(LOCATION_LNG,place.location.lng)
                putExtra(PLACE_NAME,place.name)
            }
            fragment.viewModel.savePlace(place)
            fragment.startActivity(intent)
            fragment.activity?.finish()
        }
        return viewHold
    }

    override fun getItemCount() =placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place=placeList[position]
        holder.placeAddress.text=place.address
        holder.placeName.text=place.name
    }
}