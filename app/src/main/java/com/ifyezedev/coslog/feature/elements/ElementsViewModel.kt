package com.ifyezedev.coslog.feature.elements

import androidx.lifecycle.*
import com.ifyezedev.coslog.data.db.CosLogDao
import com.ifyezedev.coslog.data.db.entities.Element
import kotlinx.coroutines.launch

class ElementsViewModel(private val db: CosLogDao) : ViewModel() {
    private val _elements = MutableLiveData<List<Element>>()
    val elements: LiveData<List<Element>> get() = _elements

    fun updateElementsLiveDataFromDb() {
        viewModelScope.launch {
            _elements.postValue(db.getAllElements())
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