package com.ifyezedev.coslog.core.io

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.util.*

data class BitmapStoreDetails(val name: String, val galleryTag: String, val path: String)

class AppBitmapStore(private val context: Context) {
    private val delimiter = "->"

    fun store(imagesUri: List<Uri>) {

    }

    fun open(imagesUri: List<Uri>): List<Bitmap> =
        context.contentResolver.openInputStream(imagesUri)


    private fun toFileName(bitmapStoreDetails: BitmapStoreDetails) = with(bitmapStoreDetails) {
        val uuid = UUID.randomUUID()
        galleryTag + delimiter + name + uuid
    }
}

private fun ContentResolver.openInputStream(uriList: List<Uri>): List<Bitmap> {
    val bitmapResult = mutableListOf<Bitmap>()
    for (i in uriList.indices) {
        val bitmapStream = this.openInputStream(uriList[i])
        val bitmap = BitmapFactory.decodeStream(bitmapStream)
        bitmapResult.add(bitmap)
    }
    return bitmapResult
}