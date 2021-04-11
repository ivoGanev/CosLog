package com.ifyezedev.coslog.feature.elements

import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.ifyezedev.coslog.core.data.BitmapHolder
import com.ifyezedev.coslog.core.extensions.loadOsGalleryBitmaps
import com.ifyezedev.coslog.core.extensions.mergeToBitmapHolders
import com.ifyezedev.coslog.feature.elements.internal.*
import com.ifyezedev.coslog.feature.elements.internal.GetBitmapsFromAndroidGalleryUseCase
import com.ifyezedev.coslog.feature.elements.internal.UriToBitmapGalleryPathConverterStandard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ElementsViewModel(
    private val openAndroidImageGalleryUseCase: OpenAndroidImageGalleryUseCase,
    private val deleteBitmapsFromInternalStorageUseCase: DeleteBitmapFromInternalStorageUseCase,
    private val loadBitmapsFromInternalStorageUseCase: LoadBitmapsFromInternalStorageUseCase,
    private val getBitmapsFromAndroidGalleryUseCase: GetBitmapsFromAndroidGalleryUseCase,
    private val saveBitmapsToInternalStorageUseCase: SaveBitmapsToInternalStorageUseCase,
    ) : ViewModel(), LifecycleObserver {

    // pending to be saved
    private var pendingBitmapCache: BitmapUriCache = BitmapUriCache()

    private val pathConverter = UriToBitmapGalleryPathConverterStandard()

    fun openAndroidImageGalleryForResult(activityForResult: (Intent, Int) -> Unit) {
        openAndroidImageGalleryUseCase.invoke(activityForResult)
    }

    fun deleteBitmapFromInternalStorage(filePath: String) {
        viewModelScope.launch {
            deleteBitmapsFromInternalStorageUseCase.invoke(filePath)
        }
    }

    fun loadBitmapsFromInternalStorage(context: Context, galleryTag: String, onResult: (List<BitmapHolder>) -> Unit) {
        viewModelScope.launch {
            loadBitmapsFromInternalStorageUseCase.invoke(context, galleryTag) { bitmapHolders ->
                onResult(bitmapHolders)
            }
        }
    }

    fun loadBitmapsFromCachedUris(context: Context, onResult: (List<BitmapHolder>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmaps = context.contentResolver.loadOsGalleryBitmaps(pendingBitmapCache.data)
            val bitmapHolders =
                bitmaps.mergeToBitmapHolders(pathConverter.toFilePaths(pendingBitmapCache.data))
            onResult(bitmapHolders)
        }
    }

    fun getBitmapsFromAndroidGallery(
        context: Context,
        intent: Intent,
        onResult: (List<BitmapHolder>) -> Unit
    ) {
        viewModelScope.launch {
            getBitmapsFromAndroidGalleryUseCase.invoke(context, intent) { bitmaps, uris ->
                pendingBitmapCache.data.addAll(uris)
                val bitmapHolders =
                    bitmaps.mergeToBitmapHolders(pathConverter.toFilePaths(uris))
                onResult(bitmapHolders)
            }
        }
    }

    fun saveBitmapsToInternalStorage(
        context: Context,
        tag: String,
    ) {
        val bitmaps = context.contentResolver.loadOsGalleryBitmaps(pendingBitmapCache.data)
        val filePaths = pathConverter.toFilePaths(pendingBitmapCache.data)
        val bitmapHolders = bitmaps.mergeToBitmapHolders(filePaths)
        pendingBitmapCache.data.clear()
        viewModelScope.launch {
            saveBitmapsToInternalStorageUseCase.invoke(context, tag, bitmapHolders)
        }
    }

    fun clearPendingUriCache() {
        pendingBitmapCache.data.clear()
    }

    class ElementsViewModelFactory(
        private val openAndroidImageGalleryUseCase: OpenAndroidImageGalleryUseCase,
        private val deleteBitmapsFromInternalStorageUseCase: DeleteBitmapFromInternalStorageUseCase,
        private val loadBitmapsFromInternalStorageUseCase: LoadBitmapsFromInternalStorageUseCase,
        private val getBitmapsFromAndroidGalleryUseCase: GetBitmapsFromAndroidGalleryUseCase,
        private val saveBitmapsToInternalStorageUseCase: SaveBitmapsToInternalStorageUseCase,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ElementsViewModel::class.java)) {
                return ElementsViewModel(
                    openAndroidImageGalleryUseCase,
                    deleteBitmapsFromInternalStorageUseCase,
                    loadBitmapsFromInternalStorageUseCase,
                    getBitmapsFromAndroidGalleryUseCase,
                    saveBitmapsToInternalStorageUseCase,
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}