package com.hsb.mygalleryapp.data.Models

import android.net.Uri

data class FoldersModel(
    var imageC: Int,
    var name: String,
    val uri: Uri,
    val id: Long
)