package com.ifyezedev.coslog.core.di.app

import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import dagger.Component

@Component(modules = [AppModule::class])
@AppScope
interface AppComponent {
    fun deleteBitmapsFromInternalStorage(): DeleteBitmapsFromInternalStorage
    fun loadBitmapsFromInternalStorage(): LoadBitmapsFromInternalStorage
    fun saveBitmapsToInternalStorage(): SaveBitmapsToInternalStorage
}