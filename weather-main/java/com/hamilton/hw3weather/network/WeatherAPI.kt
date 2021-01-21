package com.hamilton.hw3weather.network

import com.hamilton.hw3weather.data.WeatherResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


// URL: http://api.openweathermap.org/data/2.5/weather?q=London
// HOST: http://api.openweathermap.org/data/2.5
//
// PATH: /weather
//
// QUERY param separator: ?
// QUERY params: base

interface WeatherAPI {
    @GET("data/2.5/weather")
    fun getWeather(@Query("q") q: String,
                   @Query("units") units: String,
                   @Query("appid") APPID: String) : Call<WeatherResult>
}
