package com.ifyezedev.coslog.core.common

import android.app.Application
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase

class BaseApplication : Application() {

    lateinit var deleteBitmapsFromInternalStorageUseCase: DeleteBitmapsFromInternalStorageUseCase
    lateinit var loadBitmapsFromInternalStorageUseCase: LoadBitmapsFromInternalStorageUseCase
    lateinit var saveBitmapsToInternalStorageUseCase: SaveBitmapsToInternalStorageUseCase

    override fun onCreate() {
        deleteBitmapsFromInternalStorageUseCase = DeleteBitmapsFromInternalStorageUseCase()
        loadBitmapsFromInternalStorageUseCase = LoadBitmapsFromInternalStorageUseCase()
        saveBitmapsToInternalStorageUseCase = SaveBitmapsToInternalStorageUseCase()
        super.onCreate()
    }
}