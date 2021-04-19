package com.ifyezedev.coslog.core.di.activity

import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.core.di.app.AppComponent
import dagger.Component

@Component(modules = [BaseActivityModule::class], dependencies = [AppComponent::class])
@BaseActivityScope
interface BaseActivityComponent {
    fun deleteBitmapsFromInternalStorage(): DeleteBitmapsFromInternalStorage
    fun loadBitmapsFromInternalStorage(): LoadBitmapsFromInternalStorage
    fun saveBitmapsToInternalStorage(): SaveBitmapsToInternalStorage
}