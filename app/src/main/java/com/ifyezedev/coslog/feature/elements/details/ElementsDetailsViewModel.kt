package com.ifyezedev.coslog.feature.elements.details

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.core.functional.onSuccess
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromAndroidGallery
import com.ifyezedev.coslog.core.extensions.mapToUri
import com.ifyezedev.coslog.core.functional.onFailure
import com.ifyezedev.coslog.data.db.CosLogDao
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.core.common.ImageFilePathProvider
import com.ifyezedev.coslog.core.common.usecase.OpenAndroidImageGallery
import kotlinx.coroutines.launch

class ElementsDetailsViewModel(
    private val openAndroidImageGallery: OpenAndroidImageGallery,
    private val loadBitmapsFromInternalStorage: LoadBitmapsFromInternalStorage,
    private val loadBitmapsFromAndroidGallery: LoadBitmapsFromAndroidGallery,
    private val saveBitmapsToInternalStorage: SaveBitmapsToInternalStorage,
    private val imageFilePathProvider: ImageFilePathProvider,
    private val db: CosLogDao,
) : ViewModel() {

    private val _preparedPicturePaths =
        MutableLiveData<List<String>>(mutableListOf())
    val preparedPicturePaths: LiveData<List<String>>
        get() = _preparedPicturePaths

    fun updateElementInDatabase(element: Element) {
        viewModelScope.launch {
            db.updateElement(element)
        }
    }

    fun insertElementInDatabase(element: Element) {
        viewModelScope.launch {
            db.insertElement(element)
        }
    }

    fun deleteElementFromDatabase(element: Element?) {
        viewModelScope.launch {
            element?.let {
                db.deleteElement(element)
            }
        }
    }

    /**
     * Opens the Android image gallery.
     * */
    fun openAndroidImageGalleryForResult(activityForResult: (Intent, Int) -> Unit) {
        openAndroidImageGallery(viewModelScope, activityForResult)
    }

    fun prepareImagesFromAndroidGalleryForLoading(intent: Intent) {
        val uris: MutableList<Uri> = mutableListOf()
        intent.data?.let { uri -> uris.add(uri) }
        intent.clipData?.let { clipData -> uris.addAll(clipData.mapToUri()) }

        loadBitmapsFromAndroidGallery(viewModelScope, uris) { result ->
            result.onSuccess { loadedBitmaps ->
                val internalStoragePaths = uris.map { uri ->
                    imageFilePathProvider.toInternalStorageFilePath(uri)
                }

                saveBitmapsToInternalStorage(viewModelScope, internalStoragePaths.zip(loadedBitmaps)) { savedResult ->
                    savedResult.onSuccess { internalStoragePaths ->
                        val preparedPicturePaths: MutableList<String> =
                            _preparedPicturePaths.value as MutableList<String>

                        preparedPicturePaths.addAll(internalStoragePaths)
                        _preparedPicturePaths.value = preparedPicturePaths
                    }

                    savedResult.onFailure {
                        Log.e(this::class.java.simpleName, it.toString())
                    }
                }
            }
            result.onFailure {
                Log.e(this::class.java.simpleName, it.toString())
            }
        }
    }

    fun loadCachedPicturePathsWithElement(element: Element?, onResult: (List<Pair<String, Bitmap>>) -> Unit) {
        val cachedPaths = mutableListOf<String>()
        cachedPaths.addAll(preparedPicturePaths.value!!)

        if(element!=null) {
            cachedPaths.addAll(element.images)
        }
        loadBitmapsFromInternalStorage(viewModelScope, cachedPaths) { result ->
            result.onSuccess { bitmaps ->
                onResult(cachedPaths.zip(bitmaps))
            }
            result.onFailure {
                Log.e(this::class.java.simpleName, it.toString())
            }
        }
    }

    class ElementsViewModelFactory(
        private val openAndroidImageGallery: OpenAndroidImageGallery,
        private val loadBitmapsFromInternalStorage: LoadBitmapsFromInternalStorage,
        private val loadBitmapsFromAndroidGallery: LoadBitmapsFromAndroidGallery,
        private val saveBitmapsToInternalStorage: SaveBitmapsToInternalStorage,
        private val imageFilePathProvider: ImageFilePathProvider,
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
                    db
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}