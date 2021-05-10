package com.ifyezedev.coslog.core.common

import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.lang.RuntimeException
import java.util.*

/**
 * This class helps with figuring out the file name and directory of the images that
 * are stored inside the internal storage.
 * */
class ImageFilePathProvider(private val context: Context) {
    private val directoryName = "image-gallery"

    /**
     * Will convert a content provider path from the image directory to internal storage path.
     * */
    fun toInternalStorageFilePath(fileUri: Uri) =
        getDefaultImageDirectoryPath() + toAndroidGalleryName(fileUri)

    /**
     * Provides the internal storage image directory path.
     * */
    fun getDefaultImageDirectoryPath() = "${context.filesDir}/$directoryName/"

    /**
     * The main idea behind this method is to convert the content provider file path to internal storage
     * path using the system's provided unique file name.
     *
     * The content provider path coming from Android looks like:
     * content://com.android.providers.media.documents/document/image%3A26,
     * */
    private fun toAndroidGalleryName(path: Uri): String {
        val projection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
        )

        context.contentResolver
            .query(path, projection, null, null, null)?.use { cursor ->
                cursor.moveToNext()
                val uuid = UUID.randomUUID().toString()
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                return  uuid + cursor.getString(nameColumn)
            }
        throw RuntimeException("Cursor failed to load.")
    }
}