package com.ifyezedev.coslog.feature.elements.internal

import android.content.Context
import android.graphics.Bitmap
import com.ifyezedev.coslog.core.data.BitmapHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.NullPointerException

internal class SaveBitmapToInternalStorageUseCase : ManageGalleryUseCase() {
    suspend fun invoke(context: Context, bitmapHolders: List<BitmapHolder>, tag: String) {
        withContext(Dispatchers.IO) {
            val dir = File(context.filesDir, tag)
            dir.mkdirs()

            bitmapHolders.forEach { bitmapHolder ->
                invoke(bitmapHolder, dir)
            }
        }
    }

    private fun invoke(bitmapHolder: BitmapHolder, dir: File) {
        if (bitmapHolder.filePath == null) {
            throw NullPointerException("File path is required in order to save the file.")
        }

        try {
            FileOutputStream(File(dir, bitmapHolder.filePath)).use { stream ->
                bitmapHolder.bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            }
        } catch (e: IOException) {
            //TODO to handle
        }
    }
}