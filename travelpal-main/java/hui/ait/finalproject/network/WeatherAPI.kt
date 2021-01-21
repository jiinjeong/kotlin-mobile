package hui.ait.finalproject.network

import hui.ait.finalproject.data.WeatherResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("data/2.5/weather")
    fun getWeather(@Query("q") q: String,
                   @Query("units") units: String,
                   @Query("appid") APPID: String) : Call<WeatherResult>
}
