package com.ifyezedev.coslog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ifyezedev.coslog.data.db.CosLogDao
import java.lang.IllegalArgumentException

class CosplayDetailsViewModelFactory(private val dataSource: CosLogDao) : ViewModelProvider.Factory {
    //The create method's purpose is to create and return your view model.
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CosplayDetailsViewModel::class.java)){
            return CosplayDetailsViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}