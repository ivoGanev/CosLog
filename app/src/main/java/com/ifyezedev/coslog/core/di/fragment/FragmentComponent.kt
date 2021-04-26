package com.ifyezedev.coslog.core.di.fragment

import com.ifyezedev.coslog.core.common.usecase.DeleteFilesFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromAndroidGallery
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.core.di.app.AppComponent
import com.ifyezedev.coslog.core.common.ImageFilePathProvider
import dagger.Component

@Component(dependencies = [AppComponent::class])
@FragmentScope
interface FragmentComponent {
    fun deleteBitmapsFromInternalStorageUseCase(): DeleteFilesFromInternalStorage
    fun loadBitmapsFromInternalStorageUseCase(): LoadBitmapsFromInternalStorage
    fun saveBitmapsToInternalStorageUseCase(): SaveBitmapsToInternalStorage
    fun getBitmapPathsFromAndroidGallery(): LoadBitmapsFromAndroidGallery
    fun imageFilePathProvider() : ImageFilePathProvider
}