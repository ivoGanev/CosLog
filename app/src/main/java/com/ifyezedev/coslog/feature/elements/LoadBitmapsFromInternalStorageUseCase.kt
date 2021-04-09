package com.ifyezedev.coslog.feature.elements

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ifyezedev.coslog.core.data.BitmapHolder
import com.ifyezedev.coslog.core.extensions.mergeToBitmapHolders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class LoadBitmapsFromInternalStorageUseCase(
    private val galleryTag: String
) : GalleryBaseUseCase() {

    suspend fun invoke(context: Context, onResult: (List<BitmapHolder>) -> Unit) {
        withContext(Dispatchers.IO) {
            val bitmapList = mutableListOf<Bitmap>()
            val dir = File(context.filesDir, galleryTag)
            val files = dir.listFiles()

            files?.let {
                try {
                    for (i in files.indices) {
                        FileInputStream(files[i]).use { stream ->
                            val bitmap = BitmapFactory.decodeStream(stream)
                            bitmapList.add(bitmap)
                        }
                    }
                } catch (e: IOException) {
                    //TODO to handle
                }
            }
            val bitmapHolders = bitmapList.mergeToBitmapHolders(files!!.map {
                    file -> file.path
            })

            onResult(bitmapHolders)
        }
    }
}