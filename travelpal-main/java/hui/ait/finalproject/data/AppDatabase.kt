
package hui.ait.finalproject.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(Post::class, PackingItem::class, Location::class), version = 4)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDAO
    abstract fun packingItemDao(): PackingItemDAO
    abstract fun LocationDao(): LocationDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase::class.java, "tables.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }
}