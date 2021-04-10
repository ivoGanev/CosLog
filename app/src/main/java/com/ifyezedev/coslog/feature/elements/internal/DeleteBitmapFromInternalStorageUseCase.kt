package com.ifyezedev.coslog.feature.elements.internal

import com.ifyezedev.coslog.core.data.BitmapHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DeleteBitmapFromInternalStorageUseCase {
    suspend fun invoke(filePath: String) {
        withContext(Dispatchers.IO) {
            val file = File(filePath)
            file.delete()
        }
    }
}