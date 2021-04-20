package com.ifyezedev.coslog.core.di.app

import com.ifyezedev.coslog.core.common.usecase.DeleteFilesFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromAndroidGallery
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.feature.elements.internal.ImageFileProvider
import dagger.Component

@Component(modules = [AppModule::class])
@AppScope
interface AppComponent {
    fun deleteFilesFromInternalStorage(): DeleteFilesFromInternalStorage
    fun loadBitmapsFromInternalStorage(): LoadBitmapsFromInternalStorage
    fun saveBitmapsToInternalStorage(): SaveBitmapsToInternalStorage
    fun getBitmapPathsFromAndroidGallery(): LoadBitmapsFromAndroidGallery
    fun imageFilePathProvider() : ImageFileProvider
}