package hui.ait.finalproject.network

import hui.ait.finalproject.data.EmergencyResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EmergencyAPI {
    @GET("data/")
    fun getEmergency(@Query("code") code: String) : Call<EmergencyResult>
}
