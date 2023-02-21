package com.hsb.mygalleryapp.AppDao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsb.mygalleryapp.Models.FavModel

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFav(node: FavModel)

//    @Query("SELECT * FROM fav_tbl")
//    fun getAllFavImages(): LiveData<List<FavModel>>

    @Query("SELECT * FROM fav_tbl")
    fun getAllFavUri():LiveData<List<FavModel>>

    @Delete
    suspend fun deleteProject(node: FavModel)

    @Update
    suspend fun updateProject(node: FavModel)

    @Query("DELETE FROM fav_tbl")
    suspend fun deleteAllProjects()
}