package com.ifyezedev.coslog.core.di.activity

import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase
import com.ifyezedev.coslog.core.di.app.AppComponent
import dagger.Component

@Component(modules = [BaseActivityModule::class], dependencies = [AppComponent::class])
@BaseActivityScope
interface BaseActivityComponent {
    fun deleteBitmapsFromInternalStorageUseCase(): DeleteBitmapsFromInternalStorageUseCase
    fun loadBitmapsFromInternalStorageUseCase(): LoadBitmapsFromInternalStorageUseCase
    fun saveBitmapsToInternalStorageUseCase(): SaveBitmapsToInternalStorageUseCase
}