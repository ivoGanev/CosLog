package com.ifyezedev.coslog.core.common

import android.app.Application
import com.ifyezedev.coslog.feature.elements.internal.*

class BaseApplication : Application() {

    lateinit var deleteBitmapsFromInternalStorageUseCase: DeleteBitmapFromInternalStorageUseCase
    lateinit var loadBitmapsFromInternalStorageUseCase: LoadBitmapsFromInternalStorageUseCase
    lateinit var saveBitmapsToInternalStorageUseCase: SaveBitmapsToInternalStorageUseCase

    override fun onCreate() {
        deleteBitmapsFromInternalStorageUseCase = DeleteBitmapFromInternalStorageUseCase()
        loadBitmapsFromInternalStorageUseCase = LoadBitmapsFromInternalStorageUseCase()
        saveBitmapsToInternalStorageUseCase = SaveBitmapsToInternalStorageUseCase()
        super.onCreate()
    }
}