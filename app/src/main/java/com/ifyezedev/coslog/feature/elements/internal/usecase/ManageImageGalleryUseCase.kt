package com.ifyezedev.coslog.feature.elements.internal.usecase

import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage

class ManageImageGalleryUseCase {
    private lateinit var getBitmapsFromAndroidGalleryUseCase: GetBitmapsFromAndroidGalleryUseCase

    private lateinit var loadBitmapsFromInternalStorage: LoadBitmapsFromInternalStorage

    private lateinit var saveBitmapstoInternalStorage: SaveBitmapsToInternalStorage

    private lateinit var deleteBitmapsFromInternalStorage: DeleteBitmapsFromInternalStorage

    private fun inject() {
//        getBitmapFromAndroidGalleryUseCase = GetBitmapFromAndroidGalleryUseCase(bitmapHolderCache)
//        loadBitmapsFromInternalStorageUseCase =
//            LoadBitmapsFromInternalStorageUseCase(galleryTag)
//        saveBitmapstoInternalStorageUseCase = SaveBitmapToInternalStorageUseCase()
//        deleteBitmapsFromInternalStorageUseCase = DeleteBitmapFromInternalStorageUseCase()
    }
}