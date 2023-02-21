package com.hsb.mygalleryapp.data.local.AppDB

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hsb.mygalleryapp.data.local.AppDao.AppDao
import com.hsb.mygalleryapp.data.Models.FavModel


@Database(
    entities = [FavModel::class],
    version = 2,
    exportSchema = false
)
abstract class RoomDB : RoomDatabase() {
    abstract fun AppDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDB? = null
        fun getDB(context: Context): RoomDB {
            val tempInst = INSTANCE
            if (tempInst != null) {
                return tempInst
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "fav_tbl"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                INSTANCE = instance
                Log.e("DB", "$instance pata ni")
                return instance
            }
        }
    }
}