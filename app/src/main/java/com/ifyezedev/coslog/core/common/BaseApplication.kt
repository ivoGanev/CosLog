package com.ifyezedev.coslog.core.common

import android.app.Application
import com.ifyezedev.coslog.core.common.usecase.DeleteFilesFromInternalStorage
import com.ifyezedev.coslog.core.di.app.AppComponent
import com.ifyezedev.coslog.core.di.app.AppModule
import com.ifyezedev.coslog.core.di.app.DaggerAppComponent

class BaseApplication : Application() {
    // instantiate the graph
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    private lateinit var deleteFilesFromInternalStorage : DeleteFilesFromInternalStorage
    override fun onCreate() {
        // get the object from the graph
        deleteFilesFromInternalStorage = appComponent.deleteFilesFromInternalStorage()
        super.onCreate()
    }
}