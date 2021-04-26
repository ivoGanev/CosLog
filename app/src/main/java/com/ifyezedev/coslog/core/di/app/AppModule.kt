package com.ifyezedev.coslog.core.di.app

import com.ifyezedev.coslog.core.common.BaseApplication
import com.ifyezedev.coslog.core.common.usecase.DeleteFilesFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromAndroidGallery
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.core.common.ImageFilePathProvider
import dagger.Module
import dagger.Provides

@Module
class AppModule(val application: BaseApplication) {
    @Provides
    @AppScope
    fun imageFilePathProvider() : ImageFilePathProvider = ImageFilePathProvider(application)

    @Provides
    @AppScope
    fun deleteFilesFromInternalStorageUseCase(): DeleteFilesFromInternalStorage =
        DeleteFilesFromInternalStorage()

    @Provides
    @AppScope
    fun loadBitmapsFromInternalStorageUseCase(): LoadBitmapsFromInternalStorage =
        LoadBitmapsFromInternalStorage(imageFilePathProvider())

    @Provides
    @AppScope
    fun saveBitmapsToInternalStorageUseCase(): SaveBitmapsToInternalStorage =
        SaveBitmapsToInternalStorage(imageFilePathProvider())

    @Provides
    @AppScope
    fun getBitmapPathsFromAndroidGallery(): LoadBitmapsFromAndroidGallery =
        LoadBitmapsFromAndroidGallery(application)

}