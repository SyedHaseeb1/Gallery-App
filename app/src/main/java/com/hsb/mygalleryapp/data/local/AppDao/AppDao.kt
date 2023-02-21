package com.hsb.mygalleryapp.data.local.AppDao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hsb.mygalleryapp.data.Models.FavModel

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFav(node: FavModel)

    @Query("SELECT * FROM fav_tbl")
    fun getAllFavUri(): LiveData<List<FavModel>>

    @Delete
    suspend fun deleteFavImage(node: FavModel)

    @Update
    suspend fun updateProject(node: FavModel)

    @Query("SELECT uri FROM fav_tbl WHERE uri =:uri")
    fun alreadyExist(uri: String): String

    @Query("DELETE FROM fav_tbl WHERE uri =:uri")
    fun deleteExisting(uri: String)

    @Query("DELETE FROM fav_tbl")
    suspend fun deleteAllProjects()
}