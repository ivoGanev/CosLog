package com.ifyezedev.coslog.core.di.fragment

import com.ifyezedev.coslog.core.common.usecase.DeleteFileFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromAndroidGallery
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.core.di.app.AppComponent
import com.ifyezedev.coslog.feature.elements.internal.ImageFileProvider
import dagger.Component

@Component(dependencies = [AppComponent::class])
@FragmentScope
interface FragmentComponent {
    fun deleteBitmapsFromInternalStorageUseCase(): DeleteFileFromInternalStorage
    fun loadBitmapsFromInternalStorageUseCase(): LoadBitmapsFromInternalStorage
    fun saveBitmapsToInternalStorageUseCase(): SaveBitmapsToInternalStorage
    fun getBitmapPathsFromAndroidGallery(): LoadBitmapsFromAndroidGallery
    fun imageFilePathProvider() : ImageFileProvider
}