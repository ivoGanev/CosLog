package com.ifyezedev.coslog.core.common

import android.app.Application
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase
import com.ifyezedev.coslog.core.di.module.AppComponent
import com.ifyezedev.coslog.core.di.module.AppModule
import com.ifyezedev.coslog.core.di.module.DaggerAppComponent
import javax.inject.Inject

class BaseApplication : Application() {
    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    @Inject
    lateinit var deleteBitmapsFromInternalStorageUseCase: DeleteBitmapsFromInternalStorageUseCase

    @Inject
    lateinit var loadBitmapsFromInternalStorageUseCase: LoadBitmapsFromInternalStorageUseCase

    @Inject
    lateinit var saveBitmapsToInternalStorageUseCase: SaveBitmapsToInternalStorageUseCase

    override fun onCreate() {
        appComponent.inject(this)
        super.onCreate()
    }
}