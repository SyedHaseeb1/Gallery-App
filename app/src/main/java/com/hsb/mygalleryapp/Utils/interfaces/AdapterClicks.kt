package com.hsb.mygalleryapp.Utils.interfaces

import android.net.Uri

interface AdapterClicks {
    fun imageViewClick(position: Int, name: String, uri: Uri, id: Long)
}