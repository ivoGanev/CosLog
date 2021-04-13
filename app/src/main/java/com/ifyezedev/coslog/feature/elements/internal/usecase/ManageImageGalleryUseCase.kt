package com.ifyezedev.coslog.feature.elements.internal.usecase

import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorageUseCase
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorageUseCase

class ManageImageGalleryUseCase {
    private lateinit var getBitmapsFromAndroidGalleryUseCase: GetBitmapsFromAndroidGalleryUseCase

    private lateinit var loadBitmapsFromInternalStorageUseCase: LoadBitmapsFromInternalStorageUseCase

    private lateinit var saveBitmapstoInternalStorageUseCase: SaveBitmapsToInternalStorageUseCase

    private lateinit var deleteBitmapsFromInternalStorageUseCase: DeleteBitmapsFromInternalStorageUseCase

    private fun inject() {
//        getBitmapFromAndroidGalleryUseCase = GetBitmapFromAndroidGalleryUseCase(bitmapHolderCache)
//        loadBitmapsFromInternalStorageUseCase =
//            LoadBitmapsFromInternalStorageUseCase(galleryTag)
//        saveBitmapstoInternalStorageUseCase = SaveBitmapToInternalStorageUseCase()
//        deleteBitmapsFromInternalStorageUseCase = DeleteBitmapFromInternalStorageUseCase()
    }
}