package hui.ait.finalproject.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import hui.ait.finalproject.R
import hui.ait.finalproject.data.WeatherResult
import hui.ait.finalproject.network.WeatherAPI
import kotlinx.android.synthetic.main.fragment_weather.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentWeather : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.fragment_weather,
            container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weatherAPI = buildRetroFit()
        val city = getString(R.string.city)
        tvCityName.text = city
        val apiId = getString(R.string.api_id)
        val weatherCall = weatherAPI!!.getWeather(city, getString(R.string.metric_unit), apiId)
        makeWeatherCall(weatherCall, city)
    }

    companion object {
        val TAG = "FragmentClothes"
    }

    private fun buildRetroFit(): WeatherAPI? {
        val hostUrl = getString(R.string.weather_api)
        val retrofit = Retrofit.Builder()
            .baseUrl(hostUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherAPI = retrofit.create(WeatherAPI::class.java)
        return weatherAPI
    }

    private fun makeWeatherCall(
        weatherCall: Call<WeatherResult>,
        city: String?
    ) {
        weatherCall.enqueue(object : Callback<WeatherResult> {
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                tvResult.text = t.message
            }

            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                val weatherResult = getWeatherInfo(response, city)
                getWeatherImage(response)
            }
        })
    }

    private fun getWeatherInfo(
        response: Response<WeatherResult>,
        city: String?
    ): WeatherResult? {

        val weatherResult = response.body()

        tvCurWeatherDesc.text = weatherResult?.weather?.get(0)?.description
        getMetricTemperature(weatherResult)

        return weatherResult
    }
    private fun getWeatherImage(response: Response<WeatherResult>) {
        Glide.with(this)
            .load(("https://openweathermap.org/img/w/" + response.body()?.weather?.get(0)?.icon + ".png"))
            .into(imgWeather)
    }

    private fun getMetricTemperature(weatherResult: WeatherResult?) {
        tvCurTemp.text = weatherResult?.main?.temp.toString() + "\u00B0" + "C"
        tvMinTemp.text = getString(R.string.min_temp) + weatherResult?.main?.temp_min.toString() + "\u00B0" + "C"
        tvMaxTemp.text = getString(R.string.max_temp) + weatherResult?.main?.temp_max.toString() + "\u00B0" + "C"
    }
}