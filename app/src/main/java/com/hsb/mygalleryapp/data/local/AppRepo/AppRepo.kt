package com.hsb.mygalleryapp.data.local.AppRepo

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.hsb.mygalleryapp.data.local.AppDao.AppDao
import com.hsb.mygalleryapp.data.Models.FavModel
import com.hsb.mygalleryapp.data.Models.FoldersModel
import com.hsb.mygalleryapp.data.Models.ImagesModel
import java.io.File
import java.util.*

class AppRepo(var dao: AppDao, var context: Context) {
    fun addFav(node: FavModel): Boolean {
        var fav = false
        if (node.uri?.let { dao.alreadyExist(it) } == null) {
            dao.addFav(node)
            fav = true
            return fav
        } else {
            Log.d("FAV", "Already exists")
            dao.deleteExisting(node.uri!!)
        }
        return fav
    }


    var allImageList: ArrayList<ImagesModel> = ArrayList()
    var allFoldersList: ArrayList<FoldersModel> = ArrayList()


    fun getAllImages(context: Context) {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA
        )
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
                    val nameIndex =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                    val name = cursor.getString(nameIndex)

                    //Folder Names
                    val folderNameIndex =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                    val folderName = cursor.getString(folderNameIndex)

                    val folderIdIndex =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
                    val folderId = cursor.getLong(folderIdIndex)

                    //Extra
                    val tempVal = cursor.getColumnIndexOrThrow((MediaStore.Images.Media.DATA))
                    val temp = cursor.getString(tempVal)
                    val f = File(temp).parentFile?.name
                    Log.e("Album", "$f")
                    //Extra

                    var fav = false
                    val inDB = dao.alreadyExist(photoUrl.toString()) + ""
                    fav = !inDB.contains("null")
                    Log.e("Fav kabootar", "$fav")
                    val node = photoUrl?.let { ImagesModel(fav, name, it, folderId, id) }
                    val nodeFolder = photoUrl?.let { FoldersModel(0, folderName, it, folderId) }
                    if (node != null && nodeFolder != null) {
                        allImageList.add(node)
                        allFoldersList.add(nodeFolder)
                    }
                }
                var c = 0
                allFoldersList.reverse()
                val tempNode = allFoldersList.distinctBy { it.name }
                allFoldersList.clear()
                allFoldersList.addAll(tempNode)
                allFoldersList.sortBy { it.name }
            }
    }


    fun getAllFavImagesUri() = dao.getAllFavUri()

    suspend fun deleteFavImage(node: FavModel) {
        dao.deleteFavImage(node)
    }





}