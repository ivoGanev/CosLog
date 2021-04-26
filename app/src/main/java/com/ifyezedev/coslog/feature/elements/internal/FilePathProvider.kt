package com.ifyezedev.coslog.feature.elements.internal

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getIntOrNull
import java.util.*


/**
 * Converts Android gallery URIs to internal storage file path. Also contains a quick
 * way to fetch all image file paths from their directory .
 * */
class FilePathProvider(private val context: Context) {
    private val directoryName = "image-gallery"

    fun getInternalStorageFilePath(fileUri: Uri) =
        getDefaultImageDirectory() + getImageRealName(fileUri)

    fun getDefaultImageDirectory() = "${context.filesDir}/$directoryName/"

    private fun getImageRealName(path: Uri): String {
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

    fun extractFileNameFromContentProviderUriPath(uri: Uri): String =
        uri.toString().substringAfterLast("/")
}