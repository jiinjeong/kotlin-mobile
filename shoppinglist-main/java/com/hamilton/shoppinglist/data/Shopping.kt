package com.hamilton.shoppinglist.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shopping")
data class Shopping(
    @PrimaryKey(autoGenerate = true) var shoppingId : Long?,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "createDate") var createDate: String,
    @ColumnInfo(name = "shoppingText") var shoppingText: String,
    @ColumnInfo(name = "shoppingDesc") var shoppingDesc: String,
    @ColumnInfo(name = "estimatedPrice") var estimatedPrice: Int,
    @ColumnInfo(name = "boughtStatus") var boughtStatus: Boolean
) : Serializable
