package com.ifyezedev.coslog.core.io

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModelStore
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

data class BitmapDetails(
    val name: String,
    val tag: String,
)

class AppBitmapHandler(private val context: Context) {
    private val delimiter = "$$"

    fun save(bitmap: Bitmap, details: BitmapDetails) {
        val fileName = details.toFileName()

        val dir = File(context.filesDir, details.tag)
        dir.mkdirs()

        try {
            FileOutputStream(File(dir, fileName)).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            }

        } catch (e: IOException) {
            //TODO return a failure
        }
    }

    fun saveAll(bitmapsData: List<Pair<Bitmap, BitmapDetails>>) {
        bitmapsData.forEach { bitmapData ->
            save(bitmapData.first, bitmapData.second)
        }
    }

    fun openAll(tag: String): List<Bitmap> {
        val bitmapList = mutableListOf<Bitmap>()
        val dir = File(context.filesDir, tag)
        val files = dir.listFiles()!!

        try {
            for (i in files.indices) {
                FileInputStream(files[i]).use { stream ->
                    val bitmap = BitmapFactory.decodeStream(stream)
                    bitmapList.add(bitmap)
                }
            }
        } catch (e: IOException) {
            //TODO return a failure
        }
        return bitmapList
    }

    fun open(imagesUri: List<Uri>): List<Bitmap> =
        context.contentResolver.loadBitmap(imagesUri)

    private fun BitmapDetails.toFileName() = with(this) {
        val uuid = UUID.randomUUID()
        name + delimiter + uuid
    }
}

private fun ContentResolver.loadBitmap(contentProviderUris: List<Uri>): List<Bitmap> {
    val bitmapResult = mutableListOf<Bitmap>()
    for (i in contentProviderUris.indices) {
        val bitmapStream = this.openInputStream(contentProviderUris[i])
        val bitmap = BitmapFactory.decodeStream(bitmapStream)
        bitmapResult.add(bitmap)
    }
    return bitmapResult
}