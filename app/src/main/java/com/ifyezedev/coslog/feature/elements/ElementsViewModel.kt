package com.ifyezedev.coslog.feature.elements

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.core.functional.onFailure
import com.ifyezedev.coslog.core.functional.onSuccess
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromAndroidGallery
import com.ifyezedev.coslog.core.extensions.mapToUri
import com.ifyezedev.coslog.feature.elements.internal.ImageFileProvider
import com.ifyezedev.coslog.feature.elements.internal.usecase.OpenAndroidImageGalleryUseCase

class ElementsViewModel(
    private val openAndroidImageGalleryUseCase: OpenAndroidImageGalleryUseCase,
    private val loadBitmapsFromInternalStorage: LoadBitmapsFromInternalStorage,
    private val loadBitmapsFromAndroidGallery: LoadBitmapsFromAndroidGallery,
    private val saveBitmapsToInternalStorage: SaveBitmapsToInternalStorage,
    private val imageFileProvider: ImageFileProvider,
) : ViewModel(), LifecycleObserver {

    fun openAndroidImageGalleryForResult(activityForResult: (Intent, Int) -> Unit) {
        openAndroidImageGalleryUseCase.invoke(activityForResult)
    }

    fun loadBitmapsFromInternalStorage(
        vararg filePath: String,
        onResult: (List<Bitmap>) -> Unit,
    ) {
        loadBitmapsFromInternalStorage.invoke(viewModelScope, filePath.toList()) { result ->
            result.onSuccess { onResult(it) }
            result.onFailure { Log.e(this::class.java.simpleName, it.toString()) }
        }
    }

    fun onFetchImagesFromGallery(
        intent: Intent,
        onResult: (List<Uri>) -> Unit,
    ) {
        val uris: MutableList<Uri> = mutableListOf()
        intent.data?.let { uri -> uris.add(uri) }
        intent.clipData?.let { clipData -> uris.addAll(clipData.mapToUri()) }

        // savePendingBitmapPathCache.addAll(uris.map { uri -> uri.toString() })
        onResult(uris)
    }

    fun saveBitmapsToInternalStorageFromCache(bitmapPathPairs: List<Pair<String, Bitmap>>) {
        // converting from Uri provider paths to internal storage path and saving the bitmaps
        val onlyInternalStoragePaths = bitmapPathPairs
            .map { it.first }
            .filter { it.contains("content://") }
            .map { it.toUri() }

        val uriImagePathsToFilePaths = imageFileProvider.from(onlyInternalStoragePaths)
        val bitmapsOnly = bitmapPathPairs.map { it.second }

        saveBitmapsToInternalStorage(viewModelScope, uriImagePathsToFilePaths.zip(bitmapsOnly))
    }

    class ElementsViewModelFactory(
        private val openAndroidImageGalleryUseCase: OpenAndroidImageGalleryUseCase,
        private val loadBitmapsFromInternalStorage: LoadBitmapsFromInternalStorage,
        private val loadBitmapsFromAndroidGallery: LoadBitmapsFromAndroidGallery,
        private val saveBitmapsToInternalStorage: SaveBitmapsToInternalStorage,
        private val imageFileProvider: ImageFileProvider,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ElementsViewModel::class.java)) {
                return ElementsViewModel(
                    openAndroidImageGalleryUseCase,
                    loadBitmapsFromInternalStorage,
                    loadBitmapsFromAndroidGallery,
                    saveBitmapsToInternalStorage,
                    imageFileProvider,
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}