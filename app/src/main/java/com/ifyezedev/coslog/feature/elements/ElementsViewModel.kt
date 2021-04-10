package com.ifyezedev.coslog.feature.elements

import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.core.data.BitmapHolder
import com.ifyezedev.coslog.feature.elements.internal.DeleteBitmapFromInternalStorageUseCase
import com.ifyezedev.coslog.feature.elements.internal.OpenAndroidImageGalleryUseCase
import kotlinx.coroutines.launch

class ElementsViewModel(
    private val openAndroidImageGalleryUseCase: OpenAndroidImageGalleryUseCase,
    private val deleteBitmapsFromInternalStorageUseCase: DeleteBitmapFromInternalStorageUseCase
) :
    ViewModel() {

    fun openAndroidImageGallery(activityForResult: (Intent, Int) -> Unit) {
        openAndroidImageGalleryUseCase.invoke(activityForResult)
    }

    fun deleteBitmapFromInternalStorage(filePath: String) {
        viewModelScope.launch {
            deleteBitmapsFromInternalStorageUseCase.invoke(filePath)
        }
    }

    /**
     * The result is coming on the main thread
     * */
//    suspend fun loadBitmapsFromInternalStorage(onResult: (List<BitmapHolder>) {
//
//    }

    class ElementsViewModelFactory(
        private val openAndroidImageGalleryUseCase: OpenAndroidImageGalleryUseCase,
        private val deleteBitmapsFromInternalStorageUseCase: DeleteBitmapFromInternalStorageUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ElementsViewModel::class.java)) {
                return ElementsViewModel(
                    openAndroidImageGalleryUseCase,
                    deleteBitmapsFromInternalStorageUseCase
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}