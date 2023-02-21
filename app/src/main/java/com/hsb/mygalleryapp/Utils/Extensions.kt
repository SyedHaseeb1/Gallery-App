package com.hsb.mygalleryapp.Utils

import android.R.attr.data
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hsb.mygalleryapp.R


fun ImageView.setImage(uri: Uri) {
    Glide
        .with(this.context)
        .load(uri)
        .placeholder(R.drawable.ic_gallery)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

}

fun Context.rout(cls: Class<*>) {
    startActivity(Intent(this, cls))
}

fun ImageView.setThumbnail(uri: Uri, q: Float) {
    Glide
        .with(this.context)
        .load(uri)
        .override(100, 100)
        .placeholder(R.drawable.ic_gallery)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
//        .sizeMultiplier(q)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun Context.gridRv(grid: Int): RecyclerView.LayoutManager {
    return GridLayoutManager(this, grid)
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.openAnimation(view: View) {
    val animation = AnimationUtils.loadAnimation(this, R.anim.openfragment)
    view.animation = animation
}

fun Context.exitAnimation(view: View) {
    val animation = AnimationUtils.loadAnimation(this, R.anim.exitfragment)
    view.animation = animation
}


fun deletePhoto(uri: String) {

}

fun Context.getRealPath(uri: Uri): String {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor: Cursor = contentResolver.query(uri, filePathColumn, null, null, null)!!
    var yourRealPath = ""
    if (cursor.moveToFirst()) {
        val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
        yourRealPath = cursor.getString(columnIndex)
        println("Folder: $yourRealPath")
    } else {
        //boooo, cursor doesn't have rows ...
    }
    cursor.close()
    return yourRealPath
}

fun Context.isStoragePermissionGranted(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}

fun Activity.requestStoragePermission() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
        STORAGE_PERMISSION_REQUEST_CODE
    )
}
