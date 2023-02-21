package com.hsb.mygalleryapp.data.Models

import android.net.Uri

data class ImagesModel(
    var fav: Boolean,
    var name: String,
    val uri: Uri,
    val folder: Long,
    val imageId: Long
)