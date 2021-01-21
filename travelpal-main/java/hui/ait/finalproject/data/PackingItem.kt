package hui.ait.finalproject.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "packingitem")
data class PackingItem(
    @PrimaryKey(autoGenerate = true) var itemId : Long?,
    @ColumnInfo(name = "itemName") var itemName: String,
    @ColumnInfo(name = "itemQuantity") var itemQuantity: String,
    @ColumnInfo(name = "packedStatus") var packedStatus: Boolean
) : Serializable
