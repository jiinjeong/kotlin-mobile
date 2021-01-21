package hui.ait.finalproject.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "posts")
data class Post(@PrimaryKey(autoGenerate = true) var postID: Long?,
                @ColumnInfo(name = "postTitle") var postTitle: String,
                @ColumnInfo(name = "postLocation") var postLocation: String,
                @ColumnInfo(name = "postBody") var postBody: String
) : Serializable