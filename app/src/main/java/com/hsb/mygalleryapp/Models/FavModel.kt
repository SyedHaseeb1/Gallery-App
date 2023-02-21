package com.hsb.mygalleryapp.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_tbl")
data class FavModel(
    var uri: String?=null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}