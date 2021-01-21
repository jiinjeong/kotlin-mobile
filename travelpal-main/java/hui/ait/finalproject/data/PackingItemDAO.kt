package hui.ait.finalproject.data

import android.arch.persistence.room.*

@Dao
interface PackingItemDAO {
    @Query("SELECT * FROM packingitem")
    fun getAllPackingItems(): List<PackingItem>

//    @Query("SELECT * FROM packingitem WHERE city = :city")
//    fun getAllPackingItemsForCity(city: String): List<PackingItem>

    @Insert
    fun insertPackingItem(packingitem: PackingItem): Long

    @Delete
    fun deletePackingItem(packingitem: PackingItem)

    @Query("DELETE FROM packingitem")
    fun deleteAllPackingItems()

    @Update
    fun updatePackingItem(packingitem: PackingItem)
}
