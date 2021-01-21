package com.hamilton.hw3weather.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "city")
data class City(
    @PrimaryKey(autoGenerate = true) var cityId : Long?,
    @ColumnInfo(name = "City Name") var cityName: String
) : Serializable
