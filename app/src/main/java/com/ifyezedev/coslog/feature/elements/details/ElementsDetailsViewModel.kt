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
import com.ifyezedev.coslog.feature.elements.internal.FilePathProvider
import com.ifyezedev.coslog.feature.elements.internal.usecase.OpenAndroidImageGallery
import kotlinx.coroutines.launch

class ElementsDetailsViewModel(
    private val openAndroidImageGallery: OpenAndroidImageGallery,
    private val loadBitmapsFromInternalStorage: LoadBitmapsFromInternalStorage,
    private val loadBitmapsFromAndroidGallery: LoadBitmapsFromAndroidGallery,
    private val saveBitmapsToInternalStorage: SaveBitmapsToInternalStorage,
    private val db: CosLogDao,
) : ViewModel() {

    private val _loadedImagesAndPathsFromAndroidGallery =
        MutableLiveData<List<Pair<Uri, Bitmap>>>()
    val loadedImagesAndPathsFromAndroidGallery: LiveData<List<Pair<Uri, Bitmap>>>
        get() = _loadedImagesAndPathsFromAndroidGallery

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
        openAndroidImageGallery.invoke(activityForResult)
    }

    /**
     * Loads the bitmaps from the provided [element] images variable.
     * The result is paired [String] (The file path) and [Bitmap] (The actual bitmap loaded from the path).
     * */
    fun loadElementBitmapsFromInternalStorage(
        element: Element,
        onResult: (List<Pair<String, Bitmap>>) -> Unit,
    ) {
        loadBitmapsFromInternalStorage(viewModelScope, element.images) { bitmapResult ->
            bitmapResult.onSuccess { internalStorageBitmaps ->
                onResult(element.images.zip(internalStorageBitmaps))
            }
        }
    }

    /**
     * By subscribing to [loadedImagesAndPathsFromAndroidGallery] and calling this function
     * from onActivityResult(..) (in order to provide the gallery intent), you can
     * get images and their respective content provider Uri paths from the Android gallery.
     * */
    fun loadImagesFromAndroidGallery(intent: Intent) {
        val uris: MutableList<Uri> = mutableListOf()
        intent.data?.let { uri -> uris.add(uri) }
        intent.clipData?.let { clipData -> uris.addAll(clipData.mapToUri()) }

        loadBitmapsFromAndroidGallery(viewModelScope, uris) { result ->
            result.onSuccess { bitmaps ->
                _loadedImagesAndPathsFromAndroidGallery.value = uris.zip(bitmaps)
            }
        }
    }

    /**
     * @param bitmapFilePaths A pair which contains a list of the bitmap's file path [String]
     * and [Bitmap] which is the actual bitmap to save.
     *
     * When the bitmaps are successfully saved, we return the file paths. This is useful when we need to be sure
     * that the files are saved before storing the actual file path in the database.
     * */
    fun saveBitmapsToInternalStorage(
        bitmapFilePaths: List<Pair<Uri, Bitmap>>,
        onSuccessfulSave: (List<String>) -> Unit,
    ) {
        saveBitmapsToInternalStorage(viewModelScope, bitmapFilePaths) { result ->
            result.onSuccess {
                onSuccessfulSave(it)
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
        private val db: CosLogDao,

        ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ElementsDetailsViewModel::class.java)) {
                return ElementsDetailsViewModel(
                    openAndroidImageGallery,
                    loadBitmapsFromInternalStorage,
                    loadBitmapsFromAndroidGallery,
                    saveBitmapsToInternalStorage,
                    db
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}