package com.ifyezedev.coslog.feature.elements

import androidx.lifecycle.*
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromAndroidGallery
import com.ifyezedev.coslog.core.common.usecase.LoadBitmapsFromInternalStorage
import com.ifyezedev.coslog.core.common.usecase.SaveBitmapsToInternalStorage
import com.ifyezedev.coslog.data.db.CosLogDao
import com.ifyezedev.coslog.data.db.entities.Element
import com.ifyezedev.coslog.feature.elements.internal.ImageFileProvider
import com.ifyezedev.coslog.feature.elements.internal.usecase.OpenAndroidImageGalleryUseCase
import kotlinx.coroutines.launch

class ElementsViewModel(private val db: CosLogDao) : ViewModel() {
    private val _elements = MutableLiveData<List<Element>>()
    val elements: LiveData<List<Element>> get() = elements

    fun insertElement(element: Element) {
        viewModelScope.launch {
            db.insertElement(element)
        }
    }

    fun loadElements() {
        viewModelScope.launch {
            _elements.value = db.getAllElements()
        }
    }


    class ElementsViewModelFactory(
        private val db: CosLogDao,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ElementsViewModel::class.java)) {
                return ElementsViewModel(db) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}