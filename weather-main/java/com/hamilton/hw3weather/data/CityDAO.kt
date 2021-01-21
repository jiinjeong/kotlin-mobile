package com.hamilton.hw3weather.data

import android.arch.persistence.room.*

@Dao
interface CityDAO {
    @Query("SELECT * FROM city")
    fun getAllCities(): List<City>

    @Insert
    fun insertCity(city: City): Long

    @Delete
    fun deleteCity(city: City)

    @Query("DELETE FROM city")
    fun deleteAllItems()

    @Update
    fun updateCity(city: City)
}