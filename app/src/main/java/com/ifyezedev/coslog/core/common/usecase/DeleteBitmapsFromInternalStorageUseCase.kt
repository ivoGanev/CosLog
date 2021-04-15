package com.ifyezedev.coslog.core.common.usecase

import com.ifyezedev.coslog.core.data.BitmapHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DeleteBitmapsFromInternalStorageUseCase {
    suspend fun invoke(filePath: String) {
        withContext(Dispatchers.IO) {
            val file = File(filePath)
            file.delete()
        }
    }

    fun hello() {
        println("hello")
    }
}