package com.hamilton.shoppinglist.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.hamilton.shoppinglist.R

@Database(entities = arrayOf(Shopping::class), version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shoppingDao(): ShoppingDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase::class.java, context.getString(R.string.shopping))
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}