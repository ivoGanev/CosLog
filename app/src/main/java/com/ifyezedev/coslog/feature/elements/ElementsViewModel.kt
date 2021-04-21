package com.ifyezedev.coslog.feature.elements

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
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


    private val _loadedImagesAndPathsFromAndroidGallery =
        MutableLiveData<List<Pair<String, Bitmap>>>()
    val loadedImagesAndPathsFromAndroidGallery: LiveData<List<Pair<String, Bitmap>>>
        get() = _loadedImagesAndPathsFromAndroidGallery

    /**
     * Opens the Android image gallery.
     * */
    fun openAndroidImageGalleryForResult(activityForResult: (Intent, Int) -> Unit) {
        openAndroidImageGalleryUseCase.invoke(activityForResult)
    }

    /**
     * Loads the bitmaps from the internal storage.
     * The String in the result is the actual internal storage file path, and the Bitmap is the
     * loaded bitmap.
     * */
    fun loadBitmapsFromInternalStorage(onResult: (List<Pair<String, Bitmap>>) -> Unit) {
        imageFileProvider.getInternalStorageImageFilePaths()?.let { pathsToInternalStorageImages ->
            loadBitmapsFromInternalStorage(viewModelScope,
                pathsToInternalStorageImages) { bitmapResult ->
                bitmapResult.onSuccess { internalStorageBitmaps ->
                    onResult(pathsToInternalStorageImages.zip(internalStorageBitmaps))
                }
            }
        }
    }

    /**
     * This is loading images directly from the Android gallery. Subscribe to  to get the images
     * without the need of them to be saved inside the internal storage. This is good when the
     * user actually cancels the elements entry.
     * */
    fun loadImagesFromAndroidGallery(intent: Intent, onResult: (List<Pair<String, Bitmap>>) -> Unit) {
        val uris: MutableList<Uri> = mutableListOf()
        intent.data?.let { uri -> uris.add(uri) }
        intent.clipData?.let { clipData -> uris.addAll(clipData.mapToUri()) }

        loadBitmapsFromAndroidGallery(viewModelScope, uris) { result ->
            result.onSuccess { bitmaps ->
                onResult(uris.map { it.toString() }.zip(bitmaps))
            }
        }
    }

    /**
     * Saves the bitmaps to the internal storage.
     *
     * @param bitmapPathPairs A pair which contains a list of the bitmap's file path (String)
     * and Bitmap which is the actual bitmap to save.
     *
     * */
    fun saveBitmapsToInternalStorage(bitmapPathPairs: List<Pair<String, Bitmap>>) {
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