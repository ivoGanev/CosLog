package com.ifyezedev.coslog.feature.elements.details

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.ifyezedev.coslog.core.functional.onSuccess
import com.ifyezedev.coslog.core.extensions.mapToUri
import com.ifyezedev.coslog.core.functional.onFailure
import com.ifyezedev.coslog.data.db.CosLogDao
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.core.common.ImageFilePathProvider
import com.ifyezedev.coslog.core.common.usecase.*
import com.ifyezedev.coslog.core.common.usecase.core.UseCase
import com.ifyezedev.coslog.core.exception.Failure
import com.ifyezedev.coslog.core.functional.Either
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ElementsDetailsViewModel(
    private val openAndroidImageGallery: OpenAndroidImageGallery,
    private val loadBitmapsFromInternalStorage: LoadBitmapsFromInternalStorage,
    private val loadBitmapsFromAndroidGallery: LoadBitmapsFromAndroidGallery,
    private val saveBitmapsToInternalStorage: SaveBitmapsToInternalStorage,
    private val imageFilePathProvider: ImageFilePathProvider,
    private val deleteFilesFromInternalStorage: DeleteFilesFromInternalStorage,
    private val db: CosLogDao,
) : ViewModel() {

    private val _imageData =
        MediatorLiveData<MutableList<Pair<String, Bitmap>>>()

    /**
     * Contains image data (bitmap and filepath) for the pending-to-be-stored and database-stored bitmaps.
     * Observing this will keep the observer, in our case is mini-picture gallery adapter,
     * always up-to-date whether an image is added from the Android gallery which would be pending to be
     * added in the database and would also provide the database images because the adapter itself should care only to display
     * all the images no matter what happens.
     *
     */
    val imageData: MediatorLiveData<MutableList<Pair<String, Bitmap>>>
        get() = _imageData

    private val pendingImageData =
        MutableLiveData<MutableList<Pair<String, Bitmap>>>(mutableListOf())

    private val dataBaseImageData =
        MutableLiveData<MutableList<Pair<String, Bitmap>>>(mutableListOf())

    init {
        println("created")
        _imageData.addSource(pendingImageData) {
            val list1 = dataBaseImageData.value!!
            val list2 = pendingImageData.value!!
            _imageData.value = (list1 + list2) as MutableList
        }
        _imageData.addSource(dataBaseImageData) {
            val list1 = dataBaseImageData.value!!
            val list2 = pendingImageData.value!!
            _imageData.value = (list1 + list2) as MutableList
        }
    }

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

    fun deleteElement(element: Element?) {
        element?.let {
            val imagePathsCopy = mutableListOf<String>()
            imagePathsCopy.addAll(element.images)

            viewModelScope.launch {
                db.deleteElement(element)
            }
            deleteImages(imageData.value!!.map { it.first })
        }
    }

    fun loadAllImagesFromElement(element: Element?) {
        if (element != null) {
            loadBitmapsFromInternalStorage(viewModelScope, element.images) { result ->
                result.onSuccess { bitmaps ->
                    dataBaseImageData.value = element.images.zip(bitmaps).toMutableList()
                }
                result.onFailure {
                    Log.e(this::class.java.simpleName, it.toString())
                }
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
    fun addImageDataFromIntent(intent: Intent, onComplete: () -> Unit) {
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
                        val preparedImageData = pendingImageData.value as MutableList
                        preparedImageData.addAll(internalStoragePaths.zip(loadedBitmaps))
                        pendingImageData.value = preparedImageData
                        // TODO we need to wait until all images are loaded and then allow other operations
                    }

                    // if we cannot save the bitmaps to the internal storage
                    savedResult.onFailure {
                        Log.e(this::class.java.simpleName, it.toString())
                    }

                    onComplete()
                }

            }
            // if we can't load the bitmaps
            loadBitmaps.onFailure {
                Log.e(this::class.java.simpleName, it.toString())
            }
        }
    }

    fun deleteImage(position: Int, onResult: (Either<UseCase.None, Failure>) -> Unit) {
        val pathToFile = _imageData.value?.get(position)?.first!!
        deleteFilesFromInternalStorage(viewModelScope, listOf(pathToFile)) { result ->
            result.onSuccess {
                // Gets the first element of a list that matches a path.
                val getFirstPicturePathFrom: (List<Pair<String, Bitmap>>) -> Pair<String, Bitmap>? =
                    { it.firstOrNull { pair -> pair.first == pathToFile } }
                // make sure there is no trace of the image left in any of the possible containers
                // TODO:// this obviously sucks..and could be fixed by creating just a
                //          single list to store all the images and provide them with a tag
                //          in order to figure out which is pending to be saved and which is already
                //          in the database.
                _imageData.value?.remove(getFirstPicturePathFrom(_imageData.value!!))
                pendingImageData.value?.remove(getFirstPicturePathFrom(pendingImageData.value!!))
                dataBaseImageData.value?.remove(getFirstPicturePathFrom(dataBaseImageData.value!!))
            }
            onResult(result)
        }
    }

    private fun collectAllUrisFromGalleryIntent(intent: Intent): List<Uri> {
        val uris: MutableList<Uri> = mutableListOf()
        intent.data?.let { uri -> uris.add(uri) }
        intent.clipData?.let { clipData -> uris.addAll(clipData.mapToUri()) }
        return uris
    }


    override fun onCleared() {
        // TODO: delete all pending bitmaps
        println("cleared")
        deleteImages(pendingImageData.value!!.map { it.first })
        super.onCleared()
    }

    private fun deleteImages(imagePaths: List<String>) {
        deleteFilesFromInternalStorage(GlobalScope, imagePaths) {
            it.onFailure {
                Log.e(this::class.java.simpleName, "Failed to delete images from internal storage!")
            }
        }
    }

    fun clearPendingBitmapCache() {
        // when the image data gets saved to the database we need to make sure to clean it from the pendingImageData cache
        // because the files specified in it will be automatically deleted whenever we move out away from the graph
        // scope.
        pendingImageData.value?.clear()
    }
}