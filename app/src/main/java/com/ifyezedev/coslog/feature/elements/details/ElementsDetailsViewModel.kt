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

    private val _imageData =
        MutableLiveData<List<Pair<String,Bitmap>>>(mutableListOf())
    val imageData: LiveData<List<Pair<String,Bitmap>>>
        get() = _imageData

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

    /**
     * This function will open all the images selected from the Android gallery intent,
     * try to save them in the internal storage and if it successfully does then it
     * will notify all observers attached to [imageData].
     *
     * TODO:// Too many responsibilities for one method - separate the concerns.
     * */
    fun addImageData(intent: Intent) {
        val uris = collectAllUrisFromGalleryIntent(intent)

        loadBitmapsFromAndroidGallery(viewModelScope, uris) { loadBitmaps ->
            loadBitmaps.onSuccess { loadedBitmaps ->
                // create internal storage path name from the android gallery uris
                val internalStoragePathNames = uris.map { uri ->
                    imageFilePathProvider.toInternalStorageFilePath(uri)
                }

                // pack together the file names and bitmaps
                val bitmapData = internalStoragePathNames.zip(loadedBitmaps)

                // save all bitmaps to the internal storage
                saveBitmapsToInternalStorage(viewModelScope, bitmapData) { savedResult ->
                    savedResult.onSuccess { internalStoragePaths ->
                        // only if we are successfully able to save the bitmap images
                        // then we can notify all observers
                        val preparedImageData = _imageData.value as MutableList
                        preparedImageData.addAll(internalStoragePaths.zip(loadedBitmaps))
                        _imageData.value = preparedImageData
                    }

                    // if we cannot save the bitmaps to the internal storage
                    savedResult.onFailure {
                        Log.e(this::class.java.simpleName, it.toString())
                    }
                }
            }
            // if we can't load the bitmaps
            loadBitmaps.onFailure {
                Log.e(this::class.java.simpleName, it.toString())
            }
        }
    }

    private fun collectAllUrisFromGalleryIntent(intent: Intent) : List<Uri> {
        val uris: MutableList<Uri> = mutableListOf()
        intent.data?.let { uri -> uris.add(uri) }
        intent.clipData?.let { clipData -> uris.addAll(clipData.mapToUri()) }
        return uris
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