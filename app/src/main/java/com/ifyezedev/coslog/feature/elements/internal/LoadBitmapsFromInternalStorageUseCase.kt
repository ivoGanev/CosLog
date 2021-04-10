package com.ifyezedev.coslog.feature.elements.internal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.ifyezedev.coslog.core.data.BitmapHolder
import com.ifyezedev.coslog.core.extensions.mergeToBitmapHolders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class LoadBitmapsFromInternalStorageUseCase(
    private val galleryTag: String
) {
    suspend fun invoke(context: Context, onResult: (List<BitmapHolder>) -> Unit) {
        withContext(Dispatchers.IO) {
            Log.e(this::class.java.toString(), "Hello")
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
}