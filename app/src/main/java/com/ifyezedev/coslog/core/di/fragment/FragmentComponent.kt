package com.ifyezedev.coslog.core.di.fragment

import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.core.di.app.AppComponent
import dagger.Component

@Component(dependencies = [AppComponent::class])
@FragmentScope
interface FragmentComponent {
    fun deleteBitmapsFromInternalStorageUseCase(): DeleteBitmapsFromInternalStorage
    fun loadBitmapsFromInternalStorageUseCase(): LoadBitmapsFromInternalStorage
    fun saveBitmapsToInternalStorageUseCase(): SaveBitmapsToInternalStorage
}