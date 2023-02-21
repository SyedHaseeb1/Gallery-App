package com.hsb.mygalleryapp.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hsb.mygalleryapp.data.local.AppRepo.AppRepo
import com.hsb.mygalleryapp.data.Models.FavModel
import com.hsb.mygalleryapp.data.Models.FoldersModel
import com.hsb.mygalleryapp.data.Models.ImagesModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AppViewModel(application: Application, var repo: AppRepo) : AndroidViewModel(application) {
    var allImagesArray = MutableLiveData<List<ImagesModel>>()
    var allFoldersArray = MutableLiveData<List<FoldersModel>>()
    var folderImages = ArrayList<ImagesModel>()
    val context: Context = application.applicationContext
    var favColorListerFragment: ((String, Boolean) -> Unit)? = null

    var deleteAction: (() -> Unit)? = null

    init {
        repo.getAllImages(context)
        getAllImages()
        getAllFolders()
    }


    fun addFav(node: FavModel) = repo.addFav(node)

    fun getEveryThing() {
        CoroutineScope(Dispatchers.Default).launch {








        }
    }

    fun getAllFolders() {
        CoroutineScope(Dispatchers.IO).launch {
            allFoldersArray.postValue(repo.allFoldersList)
        }
    }

    fun getAllImages() {
        CoroutineScope(Dispatchers.IO).launch {
            allImagesArray.postValue(repo.allImageList)
        }
    }


    fun getFavImagesUri() = repo.getAllFavImagesUri()

    fun deleteImage(id: Long, context: Context) {
//        repo.deleteImages(id,context)
    }


}