package com.ifyezedev.coslog.core.di.app

import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import dagger.Module
import dagger.Provides

@Module
class AppModule(val application: BaseApplication) {

    @Provides
    @AppScope
    fun deleteBitmapsFromInternalStorageUseCase(): DeleteBitmapsFromInternalStorage =
        DeleteBitmapsFromInternalStorage()

    @Provides
    @AppScope
    fun loadBitmapsFromInternalStorageUseCase(): LoadBitmapsFromInternalStorage =
        LoadBitmapsFromInternalStorage()

    @Provides
    @AppScope
    fun saveBitmapsToInternalStorageUseCase(): SaveBitmapsToInternalStorage =
        SaveBitmapsToInternalStorage()
}