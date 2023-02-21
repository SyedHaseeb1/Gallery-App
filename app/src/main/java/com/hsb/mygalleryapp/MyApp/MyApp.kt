package com.hsb.mygalleryapp.MyApp

import android.app.Application
import com.hsb.mygalleryapp.Di.ImagesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(ImagesModule)
        }
    }
}