package com.ifyezedev.coslog.feature.elements.internal

import android.net.Uri
import java.util.*

internal interface UriToBitmapGalleryPathConverter {
    fun toFilePath(uri: Uri): String
    fun toFilePaths(uri: List<Uri>): List<String>
}

internal class UriToBitmapGalleryPathConverterStandard : UriToBitmapGalleryPathConverter {
    private val delimiter = "$$"

    override fun toFilePath(uri: Uri): String {
        val originalFileName = uri.toString().substringAfterLast("/")
        val uuid = UUID.randomUUID()
        return originalFileName + delimiter + uuid
    }

    override fun toFilePaths(uris: List<Uri>): List<String> {
        val result = mutableListOf<String>()
        uris.forEach { uri ->
            result.add(toFilePath(uri))
        }
        return result
    }
}