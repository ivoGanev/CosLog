package com.ifyezedev.coslog.core.di.activity

import com.ifyezedev.coslog.core.common.usecase.DeleteFilesFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.core.di.app.AppComponent
import dagger.Component

@Component(modules = [BaseActivityModule::class], dependencies = [AppComponent::class])
@BaseActivityScope
interface BaseActivityComponent {
    fun deleteBitmapsFromInternalStorage(): DeleteFilesFromInternalStorage
    fun loadBitmapsFromInternalStorage(): LoadBitmapsFromInternalStorage
    fun saveBitmapsToInternalStorage(): SaveBitmapsToInternalStorage
}