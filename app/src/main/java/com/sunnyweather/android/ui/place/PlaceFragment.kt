package com.sunnyweather.android.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.MainActivity
import com.sunnyweather.android.R
import com.sunnyweather.android.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment :Fragment() {
    val viewModel by lazy {ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter: PlaceAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place,container,false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel.isPlaceSaved()&&activity is MainActivity){
            val place=viewModel.getSavePlace()
            val intent= Intent(activity, WeatherActivity::class.java).apply {
                putExtra(PlaceAdapter.LOCATION_LAT,place.location.lat)
                putExtra(PlaceAdapter.LOCATION_LNG,place.location.lng)
                putExtra(PlaceAdapter.PLACE_NAME,place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }
        val layoutManager=LinearLayoutManager(activity)
        recycleView.layoutManager=layoutManager
        adapter= PlaceAdapter(
            this,
            viewModel.placeList
        )
        recycleView.adapter=adapter
        searchPlaceEdit.addTextChangedListener {editable ->
            val content=editable.toString()
            if (content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }else{
                recycleView.visibility=View.GONE
                bgImageView.visibility=View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }}
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer {result->
            val places=result.getOrNull()
            if (places!= null){
                recycleView.visibility=View.VISIBLE
                bgImageView.visibility=View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"???????????????????????????",Toast.LENGTH_LONG).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}