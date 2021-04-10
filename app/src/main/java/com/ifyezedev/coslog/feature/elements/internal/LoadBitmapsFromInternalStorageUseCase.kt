package com.ifyezedev.coslog.feature.elements.internal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ifyezedev.coslog.core.extensions.mergeToBitmapHolders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class LoadBitmapsFromInternalStorageUseCase(
    private val galleryTag: String
) {
    suspend fun invoke(context: Context, onResult: (List<Bitmap>, List<String>) -> Unit) {
        withContext(Dispatchers.IO) {
            val bitmaps = mutableListOf<Bitmap>()
            val dir = File(context.filesDir, galleryTag)
            val files = dir.listFiles()

            files?.let { filesNotNull ->
                try {
                    for (i in files.indices) {
                        FileInputStream(files[i]).use { stream ->
                            val bitmap = BitmapFactory.decodeStream(stream)
                            bitmaps.add(bitmap)
                        }
                    }
                    onResult(bitmaps, filesNotNull.map { file -> file.path })
                } catch (e: IOException) {
                    //TODO to handle
                }
            }
        }
    }
}