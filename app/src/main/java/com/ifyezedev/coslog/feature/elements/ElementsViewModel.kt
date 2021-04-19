package com.ifyezedev.coslog.feature.elements

import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.ifyezedev.coslog.core.common.usecase.DeleteBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.core.data.BitmapHolder
import com.ifyezedev.coslog.core.extensions.loadOsGalleryBitmaps
import com.ifyezedev.coslog.core.extensions.mergeToBitmapHolders
import com.ifyezedev.coslog.feature.elements.internal.*
import com.ifyezedev.coslog.feature.elements.internal.usecase.GetBitmapsFromAndroidGalleryUseCase
import com.ifyezedev.coslog.feature.elements.internal.UriToBitmapGalleryPathConverterStandard
import com.ifyezedev.coslog.feature.elements.internal.usecase.OpenAndroidImageGalleryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ElementsViewModel(
    private val openAndroidImageGalleryUseCase: OpenAndroidImageGalleryUseCase,
    private val deleteBitmapsFromInternalStorage: DeleteBitmapsFromInternalStorage,
    private val loadBitmapsFromInternalStorage: LoadBitmapsFromInternalStorage,
    private val getBitmapsFromAndroidGalleryUseCase: GetBitmapsFromAndroidGalleryUseCase,
    private val saveBitmapsToInternalStorage: SaveBitmapsToInternalStorage,
) : ViewModel(), LifecycleObserver {

    // TODO: Do we store bitmaps directly to the internal storage?
    private var pendingBitmapCache: BitmapUriCache = BitmapUriCache()

    private val pathConverter = UriToBitmapGalleryPathConverterStandard()

    fun openAndroidImageGalleryForResult(activityForResult: (Intent, Int) -> Unit) {
        openAndroidImageGalleryUseCase.invoke(activityForResult)
    }

    fun deleteBitmapFromInternalStorage(filePath: String) {
        deleteBitmapsFromInternalStorage(viewModelScope, filePath)
    }

    fun loadBitmapsFromInternalStorage(
        context: Context,
        galleryTag: String,
        onResult: (List<BitmapHolder>) -> Unit,
    ) {
        viewModelScope.launch {
            loadBitmapsFromInternalStorage.invoke(context, galleryTag) { bitmapHolders ->
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
        onResult: (List<BitmapHolder>) -> Unit,
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
            saveBitmapsToInternalStorage.invoke(context, tag, bitmapHolders)
        }
    }

    fun clearPendingUriCache() {
        pendingBitmapCache.data.clear()
    }

    class ElementsViewModelFactory(
        private val openAndroidImageGalleryUseCase: OpenAndroidImageGalleryUseCase,
        private val deleteBitmapsFromInternalStorage: DeleteBitmapsFromInternalStorage,
        private val loadBitmapsFromInternalStorage: LoadBitmapsFromInternalStorage,
        private val getBitmapsFromAndroidGalleryUseCase: GetBitmapsFromAndroidGalleryUseCase,
        private val saveBitmapsToInternalStorage: SaveBitmapsToInternalStorage,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ElementsViewModel::class.java)) {
                return ElementsViewModel(
                    openAndroidImageGalleryUseCase,
                    deleteBitmapsFromInternalStorage,
                    loadBitmapsFromInternalStorage,
                    getBitmapsFromAndroidGalleryUseCase,
                    saveBitmapsToInternalStorage,
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}