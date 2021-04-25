package com.ifyezedev.coslog.feature.elements.internal

import android.content.Context
import android.net.Uri
import java.util.*

/**
 * Converts Android gallery URIs to internal storage file path. Also contains a quick
 * way to fetch all image file paths from their directory .
 * */
class FilePathProvider(private val context: Context) {
    private val delimiter = "$$"
    private val formatSuffix = ".jpeg"
    private val directoryName = "image-gallery"

    fun getAbsolutePicturePath(fileName: String): String = getDefaultImageDirectory() + fileName

    fun generateFilePath(fileUri: Uri) = getDefaultImageDirectory() + generateFileName(fileUri)

    fun generateFileName(uri: Uri): String {
        val uuid = UUID.randomUUID()
        return "${extractFileNameFromContentProviderUriPath(uri)}$delimiter$uuid$formatSuffix"
    }

    fun getDefaultImageDirectory() = "${context.filesDir}/$directoryName/"

    fun extractFileNameFromContentProviderUriPath(uri: Uri): String =
        uri.toString().substringAfterLast("/")
}