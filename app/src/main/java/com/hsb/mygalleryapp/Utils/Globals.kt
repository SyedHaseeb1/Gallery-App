package com.hsb.mygalleryapp.Utils

import android.net.Uri
import android.widget.ImageView
import androidx.navigation.NavOptions
import com.hsb.mygalleryapp.R
import com.ortiz.touchview.TouchImageView

var imageClick: ((Int,String, Uri, Long) -> Unit)? = null
var fullPreview: (() -> Unit)? = null
var zoom: ((Boolean) -> Unit)? = null
var imageName: ((String) -> Unit)? = null
var touchInvoke: ((TouchImageView) -> Unit)? = null

val fragmentAnimation =
    NavOptions.Builder().setEnterAnim(R.anim.openfragment).setExitAnim(R.anim.exitfragment)
        .setPopEnterAnim(R.anim.openfragment).setPopExitAnim(R.anim.exitfragment).build()