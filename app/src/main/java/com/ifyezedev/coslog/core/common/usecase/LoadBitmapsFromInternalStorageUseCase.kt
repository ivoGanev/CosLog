package com.ifyezedev.coslog.core.common.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ifyezedev.coslog.core.data.BitmapHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class LoadBitmapsFromInternalStorageUseCase {
    suspend fun invoke(context: Context, galleryTag: String, onResult: (List<BitmapHolder>) -> Unit) {
        withContext(Dispatchers.IO) {
            val bitmapHolders = mutableListOf<BitmapHolder>()
            val dir = File(context.filesDir, galleryTag)
            val files = dir.listFiles()

            files?.let {  filesNotNull ->
                try {
                    for (i in files.indices) {
                        FileInputStream(files[i]).use { stream ->
                            val bitmap = BitmapFactory.decodeStream(stream)
                            bitmapHolders.add(BitmapHolder(bitmap, filesNotNull[i].path))
                        }
                    }
                } catch (e: IOException) {
                    //TODO to handle
                }
            }
            onResult(bitmapHolders)
        }
    }

    suspend fun invoke(filePath: String, onResult: (Bitmap) -> Unit) {
        withContext(Dispatchers.IO) {
            FileInputStream(File(filePath)).use { stream ->
                onResult(BitmapFactory.decodeStream(stream))
            }
        }
    }
}