package com.ifyezedev.coslog.core.io

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.*
import java.util.*

data class BitmapDetails(
    val name: String,
    val tag: String,
)

class BitmapResolver(private val context: Context) {
    private val delimiter = "$$"

    fun save(uri: Uri, tag: String) {
        val originalFileName = uri.toString().substringAfterLast("/")
        val uuid = UUID.randomUUID()
        val fileName = originalFileName + delimiter + uuid
        val dir = File(context.filesDir, tag)
        val bitmap = context.contentResolver.loadBitmap(uri)

        dir.mkdirs()

        try {
            FileOutputStream(File(dir, fileName)).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            }

        } catch (e: IOException) {
            //TODO to handle
        }
    }

    fun saveAll(uris: List<Uri>, tag: String) {
        uris.forEach { uri ->
            save(uri, tag)
        }
    }

    fun openAll(tag: String): List<Bitmap> {
        val bitmapList = mutableListOf<Bitmap>()
        val dir = File(context.filesDir, tag)
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
        return bitmapList
    }

    fun open(imagesUri: List<Uri>): List<Bitmap> =
        context.contentResolver.loadBitmaps(imagesUri)
}


private fun ContentResolver.loadBitmap(contentProviderUri: Uri): Bitmap {
    val bitmapStream = this.openInputStream(contentProviderUri)
    return BitmapFactory.decodeStream(bitmapStream)
}

private fun ContentResolver.loadBitmaps(contentProviderUris: List<Uri>): List<Bitmap> {
    val bitmapResult = mutableListOf<Bitmap>()
    for (i in contentProviderUris.indices) {
        val bitmapStream = this.openInputStream(contentProviderUris[i])
        val bitmap = BitmapFactory.decodeStream(bitmapStream)
        bitmapResult.add(bitmap)
    }
    return bitmapResult
}