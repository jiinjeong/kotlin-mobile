package com.hamilton.hw3weather

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.hamilton.hw3weather.data.WeatherResult
import com.hamilton.hw3weather.network.WeatherAPI
import kotlinx.android.synthetic.main.activity_weather_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherDetailsActivity : AppCompatActivity() {

    private val HOST_URL = "http://api.openweathermap.org"
    private val API_ID = "5a13d96e29f35760c21e1d7ec909ff90"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details)

        val retrofit = Retrofit.Builder()
            .baseUrl(HOST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherAPI = retrofit.create(WeatherAPI::class.java)

        val city = getIntent().getStringExtra("KEY_CITY")
        val weatherCall = weatherAPI.getWeather(city, "metric", API_ID)

        weatherCall.enqueue(object : Callback<WeatherResult> {

            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                tvCityName.text = city
                tvResult.text = t.message
            }

            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                val weatherResult = response.body()
                tvCityName.text = city

                tvCurWeatherDesc.text = weatherResult?.weather?.get(0)?.description
                tvCurTemp.text = weatherResult?.main?.temp.toString() + "\u00B0"
                tvMinTemp.text = "Min Temp: " + weatherResult?.main?.temp_min.toString() + "\u00B0"
                tvMaxTemp.text = "Max Temp: " + weatherResult?.main?.temp_max.toString() + "\u00B0"
                tvHumidity.text = "Humidity: " + weatherResult?.main?.humidity.toString()
                tvWindSpeed.text = "Wind Speed: " + weatherResult?.wind?.speed.toString()
                tvSunrise.text = "Sunrise: " + weatherResult?.sys?.sunrise.toString()
                tvSunset.text = "Sunset: " + weatherResult?.sys?.sunset.toString()

                if (tvCurTemp.text == "null" + "\u00B0"){
                    tvResult.text = "No city found."
                }

                Glide.with(this@WeatherDetailsActivity)
                    .load( ("https://openweathermap.org/img/w/" + response.body()?.weather?.get(0)?.icon + ".png"))
                    .into(imgWeather)
            }
        })
    }
}
