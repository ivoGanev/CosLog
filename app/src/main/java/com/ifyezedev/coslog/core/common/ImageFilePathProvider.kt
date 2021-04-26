package com.ifyezedev.coslog.core.common

import android.content.Context
import android.net.Uri
import android.provider.MediaStore


class ImageFilePathProvider(private val context: Context) {
    private val directoryName = "image-gallery"

    fun toInternalStorageFilePath(fileUri: Uri) =
        getDefaultImageDirectory() + toAndroidGalleryName(fileUri)

    fun getDefaultImageDirectory() = "${context.filesDir}/$directoryName/"

    private fun toAndroidGalleryName(path: Uri): String {
        val projection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
        )
        context.contentResolver
            .query(path, projection, null, null, null)!!.use { cursor ->
                cursor.moveToNext()
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                return cursor.getString(nameColumn)
            }
    }
}