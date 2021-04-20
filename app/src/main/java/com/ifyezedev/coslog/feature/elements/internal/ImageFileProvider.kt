package com.ifyezedev.coslog.feature.elements.internal

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.*

interface FilePathProvider {
    fun from(uri: Uri): String
    fun from(uris: List<Uri>): List<String>
}

/**
 * Converts Android gallery URIs to internal storage file path. Also contains a quick
 * way to fetch all image file paths from their directory .
 * */
class ImageFileProvider(private val context: Context) : FilePathProvider {
    private val delimiter = "$$"
    private val formatSuffix = ".jpeg"
    private val directoryName = "image-gallery"

    private val imageDirectory get() =  "${context.filesDir}/$directoryName/"

    fun getInternalStorageImageFilePaths() : List<String>? {
        val files = File(imageDirectory)
        return files.listFiles()?.map { file -> file.path }
    }

    override fun from(uri: Uri): String {
        val originalFileName = uri.toString().substringAfterLast("/")
        val uuid = UUID.randomUUID()
        return "${context.filesDir}/$directoryName/$originalFileName$delimiter$uuid$formatSuffix"
    }

    override fun from(uris: List<Uri>): List<String> {
        val result = mutableListOf<String>()
        uris.forEach { uri ->
            result.add(from(uri))
        }
        return result
    }
}