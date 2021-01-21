package com.hamilton.hw3weather.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hamilton.hw3weather.CityScrollingActivity
import com.hamilton.hw3weather.R
import com.hamilton.hw3weather.WeatherDetailsActivity
import com.hamilton.hw3weather.data.AppDatabase
import com.hamilton.hw3weather.data.City
import com.hamilton.hw3weather.touch.CityTouchHelperCallback
import kotlinx.android.synthetic.main.city_row.view.*
import java.util.*

class CityAdapter : RecyclerView.Adapter<CityAdapter.ViewHolder>, CityTouchHelperCallback {

    private var cityList = mutableListOf<City>()
    private val context: Context

    constructor(context: Context, listCity: List<City>) : super() {
        this.context = context
        cityList.addAll(listCity)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCityName = itemView.tvCityName
        var btnDelete = itemView.btnDelete
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val cityRowView = LayoutInflater.from(context).inflate(
            R.layout.city_row, viewGroup, false
        )
        return ViewHolder(cityRowView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val city = cityList.get(position)

        setViewHolder(viewHolder, city)
        buttonFunctions(viewHolder)
    }

    private fun setViewHolder(
        viewHolder: ViewHolder,
        city: City
    ) {
        viewHolder.tvCityName.text = city.cityName
        viewHolder.tvCityName.setOnClickListener {
            startWeatherDetailsActivity(city)
        }
    }

    private fun buttonFunctions(
        viewHolder: ViewHolder
    ) {
        viewHolder.btnDelete.setOnClickListener {
            deleteCity(viewHolder.adapterPosition)
        }
    }

    private fun startWeatherDetailsActivity(city: City) {
        var intentDetails = Intent(
            context, WeatherDetailsActivity::class.java
        )
        intentDetails.putExtra(context.getString(R.string.city_key), city.cityName)
        context.startActivity(intentDetails)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    fun addCity(city: City) {
        cityList.add(0, city)
        notifyItemInserted(0)
    }

    fun updateCity(city: City, editIndex: Int) {
        cityList.set(editIndex, city)
        notifyItemChanged(editIndex)
    }

    private fun deleteCity(deletePosition: Int) {
        Thread {
            AppDatabase.getInstance(context).cityDao().deleteCity(cityList.get(deletePosition))
            (context as CityScrollingActivity).runOnUiThread {
                cityList.removeAt(deletePosition)
                notifyItemRemoved(deletePosition)
            }
        }.start()
    }

    fun deleteAllItems() {
        Thread {
            AppDatabase.getInstance(context).cityDao().deleteAllItems()
            (context as CityScrollingActivity).runOnUiThread {
                cityList.clear()
                notifyDataSetChanged()
            }
        }.start()
    }

    override fun onDismissed(position: Int) {
        deleteCity(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(cityList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}
