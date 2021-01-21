package hui.ait.finalproject.data

import android.arch.persistence.room.*

@Dao
interface PostDAO {
    @Query("SELECT * FROM posts")
    fun getAllPosts() : List<Post>

    @Query("SELECT * FROM posts WHERE postLocation = :city")
    fun getAllPostCity(city:String) : List<Post>

    @Insert
    fun insertPost(post : Post): Long

    @Delete
    fun deletePost(vararg post : Post)

    @Update
    fun updatePost(vararg post : Post)

}