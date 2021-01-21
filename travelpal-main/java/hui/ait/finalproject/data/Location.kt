package hui.ait.finalproject.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "locations")
data class Location(
    @PrimaryKey() var location : String,
    @ColumnInfo(name = "budget") var budget: Int,
    @ColumnInfo(name = "food") var food: Int,
    @ColumnInfo(name = "travel") var travel: Int,
    @ColumnInfo(name = "lodging") var lodging: Int,
    @ColumnInfo(name = "entertainment") var entertainment: Int


) : Serializable