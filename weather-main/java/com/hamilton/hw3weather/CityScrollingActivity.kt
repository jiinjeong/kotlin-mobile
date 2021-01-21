package com.hamilton.hw3weather

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import com.hamilton.hw3weather.adapter.CityAdapter
import com.hamilton.hw3weather.data.AppDatabase
import com.hamilton.hw3weather.data.City
import com.hamilton.hw3weather.dialog.CityDialog
import com.hamilton.hw3weather.touch.CityRecyclerTouchCallback
import kotlinx.android.synthetic.main.activity_city_scrolling.*

class CityScrollingActivity : AppCompatActivity(), CityDialog.CityHandler {

    private lateinit var cityAdapter : CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_scrolling)

        setSupportActionBar(toolbar)
        initRecyclerViewFromDB()

        fab.setOnClickListener {
            CityDialog().show(supportFragmentManager, getString(R.string.add_dialog))
        }
    }

    private fun initRecyclerViewFromDB() {
        Thread {
            var cityList = AppDatabase.getInstance(this@CityScrollingActivity).cityDao().getAllCities()

            runOnUiThread {
                cityAdapter = CityAdapter(this, cityList)
                createRecycler()

                val callback = CityRecyclerTouchCallback(cityAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerCity)
            }
        }.start()
    }

    private fun createRecycler() {
        recyclerCity.layoutManager = LinearLayoutManager(this)
        recyclerCity.adapter = cityAdapter

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerCity.addItemDecoration(itemDecoration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_city_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete_list){
            deleteList()}
        return true
    }

    private fun deleteList(){
        cityAdapter.deleteAllItems()
    }

    override fun cityCreated(item: City) {
        Thread {
            var newId = AppDatabase.getInstance(this).cityDao().insertCity(item)
            item.cityId = newId
            runOnUiThread {
                cityAdapter.addCity(item)
            }
        }.start()
    }

    override fun cityUpdated(item: City) {
        Thread {
            AppDatabase.getInstance(this).cityDao().updateCity(item)
            runOnUiThread {
                cityAdapter.updateCity(item, -1)
            }
        }.start()
    }
}
