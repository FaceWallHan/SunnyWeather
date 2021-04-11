package com.sunnyweather.android.ui.weather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import com.sunnyweather.android.ui.place.PlaceAdapter
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import kotlinx.android.synthetic.main.now.placeName
import kotlinx.android.synthetic.main.place_item.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this).get(WeatherModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView=window.decorView
        decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor=Color.TRANSPARENT
        //背景图与状态栏融合
        setContentView(R.layout.activity_weather)
        if (viewModel.locationLng.isEmpty()){
            viewModel.locationLng=intent.getStringExtra(PlaceAdapter.LOCATION_LNG)?:""
        }
        if (viewModel.locationLat.isEmpty()){
            viewModel.locationLat=intent.getStringExtra(PlaceAdapter.LOCATION_LAT)?:""
        }
        if (viewModel.placeName.isEmpty()){
            viewModel.placeName=intent.getStringExtra(PlaceAdapter.PLACE_NAME)?:""
        }
        viewModel.weatherLiveData.observe(this, Observer { result->
            val weather=result.getOrNull()
            if (weather!=null){
                showWeatherInfo(weather)
            }else{
               Toast.makeText(this,"",Toast.LENGTH_LONG).show()
               result.exceptionOrNull()?.printStackTrace()
            }
         })
    }
    private fun showWeatherInfo(weather:Weather){
        placeName.text=viewModel.placeName
        val realtime=weather.realtime
        val daily=weather.daily
        //填充now布局中的数据
        val currentTempText="${realtime.temperature.toInt()}℃"
        currentTemp.text=currentTempText
        currentSky.text= getSky(realtime.skyCon)?.info
        val currentPm25Text="空气指数${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text=currentPm25Text
        getSky(realtime.skyCon)?.bg?.let { nowLayout.setBackgroundResource(it) }
        //填充fore_Cast布局中的数据
        forecastLayout.removeAllViews()
        val days=daily.skycon.size
        for (i in 0 until days){
            val skycon=daily.skycon[i]
            val temperature=daily.temperature[i]
            val view=LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false)
            val dateInfo=view.findViewById<TextView>(R.id.dataInfo)
            val skyIcon=view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo=view.findViewById<TextView>(R.id.skyInfo)
            val temperateInfo=view.findViewById<TextView>(R.id.temperateInfo)
            val simpleDateFormat=SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text=simpleDateFormat.format(skycon.data)
            val sky= getSky(skycon.value)
            sky?.icon?.let {  skyIcon.setImageResource(it)}
            skyInfo.text=sky?.info
            val tempText="${temperature.min.toInt()}-${temperature.max.toInt()}℃"
            temperateInfo.text=tempText
            forecastLayout.addView(view)
        }
        //填充life_index布局中的数据
        val lifeIndex=daily.lifeIndex
        coldRiskText.text=lifeIndex.coldRisk[0].desc
        dressingText.text=lifeIndex.dressing[0].desc
        ultravioletText.text=lifeIndex.ultraviolet[0].desc
        carwashingText.text=lifeIndex.carWashing[0].desc
        weatherLayout.visibility= View.VISIBLE
    }
}