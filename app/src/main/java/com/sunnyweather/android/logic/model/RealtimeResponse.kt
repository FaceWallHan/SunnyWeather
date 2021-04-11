package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

class RealtimeResponse(val status:String,val result:Result) {
    data class Result(val realTime: Realtime)
    data class Realtime(val skyCon:String,val temperature:Float,
                        @SerializedName("qir_quality") val airQuality: AirQuality)
    data class AirQuality(val aqi:AQI)
    data class AQI(val chn:Float)
}