package com.hamilton.shoppinglist.data

import android.arch.persistence.room.*

@Dao
interface ShoppingDAO {
    @Query("SELECT * FROM shopping")
    fun getAllShoppings(): List<Shopping>

    @Insert
    fun insertShopping(shopping: Shopping): Long

    @Delete
    fun deleteShopping(shopping: Shopping)

    @Query("DELETE FROM shopping")
    fun deleteAllItems()

    @Update
    fun updateShopping(shopping: Shopping)
}