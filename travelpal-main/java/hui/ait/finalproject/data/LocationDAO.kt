package hui.ait.finalproject.data

import android.arch.persistence.room.*


@Dao
interface LocationDAO {
    @Query("SELECT * FROM locations")
    fun getAllLocations(): List<Location>

    @Insert
    fun insertLocation(todo: Location): Long

    @Delete
    fun deleteLocation(todo: Location)

    @Query("DELETE FROM locations")
    fun deleteAll()

    @Query("SELECT * FROM locations WHERE location LIKE :search")
    fun findLocationWithName(search: String): Location

    @Update
    fun updateLocation(todo: Location)
}