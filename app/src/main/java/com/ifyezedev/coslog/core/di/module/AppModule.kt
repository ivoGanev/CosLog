package com.ifyezedev.coslog.core.di.module

import android.app.Application
import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase
import dagger.Module
import dagger.Provides

@Module
class AppModule(val application: BaseApplication) {

    @Provides
    @AppScope
    fun deleteBitmapsFromInternalStorageUseCase(): DeleteBitmapsFromInternalStorageUseCase =
        DeleteBitmapsFromInternalStorageUseCase()

    @Provides
    @AppScope
    fun loadBitmapsFromInternalStorageUseCase(): LoadBitmapsFromInternalStorageUseCase =
        LoadBitmapsFromInternalStorageUseCase()

    @Provides
    @AppScope
    fun saveBitmapsToInternalStorageUseCase(): SaveBitmapsToInternalStorageUseCase =
        SaveBitmapsToInternalStorageUseCase()
}