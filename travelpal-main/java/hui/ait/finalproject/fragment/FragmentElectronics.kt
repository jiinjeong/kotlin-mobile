package hui.ait.finalproject.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hui.ait.finalproject.R
import hui.ait.finalproject.data.EmergencyResult
import hui.ait.finalproject.network.EmergencyAPI
import kotlinx.android.synthetic.main.fragment_electronics.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentElectronics : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.fragment_electronics,
            container, false)
    }

    companion object {
        val TAG = "FragmentElectronics"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emergencyAPI = buildRetroFit()
        val country = getString(R.string.Hungary)
        val emergencyCall = emergencyAPI!!.getEmergency(country)
        // API not working:
        //  makeEmergencyCall(emergencyCall)
        tvFire.text = getString(R.string.fire_num) + getString(R.string.one_five)
        tvAmbulance.text = getString(R.string.amb_num) + getString(R.string.one_four)
        tvPolice.text = getString(R.string.police_num) + getString(R.string.one_seven)

        btnEmergency.setOnClickListener {
            val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
                type = ContactsContract.RawContacts.CONTENT_TYPE
            }
            var phoneNumber = getString(R.string.sample_phone_112)

            intent.apply {
                putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber)
                putExtra(
                    ContactsContract.Intents.Insert.PHONE_TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_WORK
                )
            }
            startActivity(intent)
        }
    }

    private fun buildRetroFit(): EmergencyAPI? {
        val hostUrl = getString(R.string.emer_api)

        val retrofit = Retrofit.Builder()
            .baseUrl(hostUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val emergencyAPI = retrofit.create(EmergencyAPI::class.java)
        return emergencyAPI
    }

    private fun makeEmergencyCall(emergencyCall: Call<EmergencyResult>) {
        emergencyCall.enqueue(object : Callback<EmergencyResult> {
            override fun onFailure(call: Call<EmergencyResult>, t: Throwable) {
                tvEmergencyCallResult.text = t.message
            }

            override fun onResponse(call: Call<EmergencyResult>, response: Response<EmergencyResult>) {
                getEmergencyInfo(response)
            }
        })
    }

    private fun getEmergencyInfo(
        response: Response<EmergencyResult>): EmergencyResult? {

        val emergencyResult = response.body()
        displayEmergencyNumbers(emergencyResult)
        return emergencyResult
    }

    private fun displayEmergencyNumbers(emergencyResult: EmergencyResult?) {
        tvFire.text = getString(R.string.fire) + emergencyResult?.Fire?.All.toString()
        tvAmbulance.text = getString(R.string.ambulance) + emergencyResult?.Ambulance?.All.toString()
        tvPolice.text = getString(R.string.police) + emergencyResult?.Police?.All.toString()
    }
}
