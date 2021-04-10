package com.ifyezedev.coslog.feature.elements

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ifyezedev.coslog.core.data.BitmapHolder
import com.ifyezedev.coslog.core.extensions.loadOsGalleryBitmaps
import com.ifyezedev.coslog.core.extensions.mergeToBitmapHolders
import com.ifyezedev.coslog.feature.elements.internal.*
import com.ifyezedev.coslog.feature.elements.internal.GetBitmapsFromAndroidGalleryUseCase
import com.ifyezedev.coslog.feature.elements.internal.UriToBitmapGalleryPathConverterStandard
import kotlinx.coroutines.launch

class ElementsViewModel(
    private val openAndroidImageGalleryUseCase: OpenAndroidImageGalleryUseCase,
    private val deleteBitmapsFromInternalStorageUseCase: DeleteBitmapFromInternalStorageUseCase,
    private val loadBitmapsFromInternalStorageUseCase: LoadBitmapsFromInternalStorageUseCase,
    private val getBitmapsFromAndroidGalleryUseCase: GetBitmapsFromAndroidGalleryUseCase,
    private val saveBitmapsToInternalStorageUseCase: SaveBitmapsToInternalStorageUseCase,

    ) :
    ViewModel() {

    private var bitmapUriCache: BitmapUriCache = BitmapUriCache()

    private val pathConverter = UriToBitmapGalleryPathConverterStandard()

    fun openAndroidImageGallery(activityForResult: (Intent, Int) -> Unit) {
        openAndroidImageGalleryUseCase.invoke(activityForResult)
    }

    fun deleteBitmapFromInternalStorage(filePath: String) {
        viewModelScope.launch {
            deleteBitmapsFromInternalStorageUseCase.invoke(filePath)
        }
    }

    fun loadBitmapsFromInternalStorage(context: Context, onResult: (List<BitmapHolder>) -> Unit) {
        viewModelScope.launch {
            loadBitmapsFromInternalStorageUseCase.invoke(context) { bitmapHolders ->
                onResult(bitmapHolders)
            }
        }
    }

    fun getBitmapsFromAndroidGallery(
        context: Context,
        intent: Intent,
        onResult: (List<BitmapHolder>) -> Unit
    ) {
        viewModelScope.launch {
            getBitmapsFromAndroidGalleryUseCase.invoke(context, intent) { bitmaps, uris ->
                bitmapUriCache.data.addAll(uris)
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
        val bitmaps = context.contentResolver.loadOsGalleryBitmaps(bitmapUriCache.data)
        val filePaths = pathConverter.toFilePaths(bitmapUriCache.data)
        val bitmapHolders = bitmaps.mergeToBitmapHolders(filePaths)
        bitmapUriCache.data.clear()
        viewModelScope.launch {
            saveBitmapsToInternalStorageUseCase.invoke(context, tag, bitmapHolders)
        }
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