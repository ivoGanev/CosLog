package com.ifyezedev.coslog.core.common.usecase

import android.content.Context
import android.graphics.Bitmap
import com.ifyezedev.coslog.core.data.BitmapHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SaveBitmapsToInternalStorage  {

    // TODO: make it return the file path
    suspend fun invoke(context: Context, tag: String, bitmapHolders: List<BitmapHolder>) {
        withContext(Dispatchers.IO) {
            val dir = File(context.filesDir, tag)
            dir.mkdirs()

            bitmapHolders.forEach { bitmapHolder ->
                //println(tag + bitmapHolder.filePath)
                invoke(bitmapHolder, dir)
            }
        }
    }

    private fun invoke(bitmapHolder: BitmapHolder, dir: File) {
        try {
            FileOutputStream(File(dir, bitmapHolder.filePath)).use { stream ->
                bitmapHolder.bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            }
        } catch (e: IOException) {
            //TODO to handle
        }
    }
}