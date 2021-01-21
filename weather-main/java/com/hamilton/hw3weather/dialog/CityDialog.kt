package com.hamilton.hw3weather.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.hamilton.hw3weather.CityScrollingActivity
import com.hamilton.hw3weather.R
import com.hamilton.hw3weather.data.City
import kotlinx.android.synthetic.main.new_city_dialog.view.*
import java.lang.RuntimeException
import java.util.*

class CityDialog : DialogFragment() {

    private lateinit var etCityName: EditText
    private lateinit var cityHandler: CityHandler

    interface CityHandler {
        fun cityCreated(item: City)
        fun cityUpdated(item: City)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is CityHandler) {
            cityHandler = context
        } else {
            throw RuntimeException(
                getString(R.string.exception_cityhandler)) as Throwable
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = build()

        builder.setPositiveButton((getString(R.string.ok))) { dialog, witch -> }
        builder.setNegativeButton((getString(R.string.close))) { dialog, witch -> }
        return builder.create()
    }

    private fun build(): AlertDialog.Builder {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.city_name))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_city_dialog, null
        )
        builder.setView(rootView)
        etCityName = rootView.etCityName
        return builder
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        checkError(positiveButton)
    }

    private fun checkError(positiveButton: Button) {
        positiveButton.setOnClickListener {
            if (etCityName.text.isNotEmpty()) {
                createCity()
            } else if (etCityName.text.isEmpty()) {
                etCityName.error = getString(R.string.exception_blank)
            }
        }
    }

    private fun createCity() {
        cityHandler.cityCreated(
            City(null,
                etCityName.text.toString())
        )
        dialog.dismiss()
    }
}
