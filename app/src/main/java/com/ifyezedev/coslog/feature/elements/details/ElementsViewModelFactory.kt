package com.ifyezedev.coslog.feature.elements.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ifyezedev.coslog.core.common.ImageFilePathProvider
import com.ifyezedev.coslog.core.common.usecase.*
import com.ifyezedev.coslog.data.db.CosLogDao

class ElementsViewModelFactory(
    private val openAndroidImageGallery: OpenAndroidImageGallery,
    private val loadBitmapsFromInternalStorage: LoadBitmapsFromInternalStorage,
    private val loadBitmapsFromAndroidGallery: LoadBitmapsFromAndroidGallery,
    private val saveBitmapsToInternalStorage: SaveBitmapsToInternalStorage,
    private val imageFilePathProvider: ImageFilePathProvider,
    private val deleteFilesFromInternalStorage: DeleteFilesFromInternalStorage,
    private val db: CosLogDao,

    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElementsDetailsViewModel::class.java)) {
            return ElementsDetailsViewModel(
                openAndroidImageGallery,
                loadBitmapsFromInternalStorage,
                loadBitmapsFromAndroidGallery,
                saveBitmapsToInternalStorage,
                imageFilePathProvider,
                deleteFilesFromInternalStorage,
                db
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}