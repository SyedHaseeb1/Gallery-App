package com.hsb.mygalleryapp.AppRepo

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hsb.mygalleryapp.AppDao.AppDao
import com.hsb.mygalleryapp.Models.FavModel
import com.hsb.mygalleryapp.Models.ImagesModel

class AppRepo(var dao: AppDao) {
    suspend fun addFav(node: FavModel) {
        dao.addFav(node)
    }

    fun getAllImages(context: Context): MutableLiveData<List<ImagesModel>> {
        val allImagesArray = MutableLiveData<List<ImagesModel>>()
        val images = ArrayList<ImagesModel>()
        val projection = arrayOf(MediaStore.Images.Media._ID)

        context.contentResolver
            .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)
            .use { cursor ->
                val idColumn = cursor?.getColumnIndex(MediaStore.Images.Media._ID)
                while (cursor?.moveToNext() == true) {
                    val id = idColumn?.let { cursor.getLong(it) }
                    val photoUrl = id?.let {
                        ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            it
                        )
                    }
                    val node = photoUrl?.let { ImagesModel(false, it) }
                    if (node != null) {
                        images.add(node)
                    }
                    allImagesArray.postValue(images)
                }
            }
        return allImagesArray
    }

//    fun getAllFavImages() = dao.getAllFavImages()
    fun getAllFavImagesUri() = dao.getAllFavUri()

//    suspend fun delete(node: ImagesModel) {
//        dao.deleteProject(node)
//    }
//
//    suspend fun updateProject(node: ImagesModel) {
//        dao.updateProject(node)
//    }
//
//    suspend fun deleteAllProjects() {
//        dao.deleteAllProjects()
//    }
}