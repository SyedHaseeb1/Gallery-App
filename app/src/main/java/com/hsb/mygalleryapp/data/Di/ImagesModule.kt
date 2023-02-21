package com.hsb.mygalleryapp.data.Di

import android.app.Application
import com.hsb.mygalleryapp.data.local.AppDB.RoomDB
import com.hsb.mygalleryapp.data.local.AppRepo.AppRepo
import com.hsb.mygalleryapp.ViewModel.AppViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val ImagesModule = module {
    single { RoomDB.getDB(androidContext() as Application).AppDao() }
    single { AppRepo(dao = get(), context = androidContext() as Application) }
    single { AppViewModel(androidContext() as Application,repo=get()) }
}