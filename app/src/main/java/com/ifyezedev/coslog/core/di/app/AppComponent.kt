package com.ifyezedev.coslog.core.di.app

import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase
import dagger.Component
import dagger.Provides

@Component(modules = [AppModule::class])
@AppScope
interface AppComponent {
    fun deleteBitmapsFromInternalStorageUseCase(): DeleteBitmapsFromInternalStorageUseCase
    fun loadBitmapsFromInternalStorageUseCase(): LoadBitmapsFromInternalStorageUseCase
    fun saveBitmapsToInternalStorageUseCase(): SaveBitmapsToInternalStorageUseCase
}