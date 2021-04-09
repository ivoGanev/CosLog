package com.ifyezedev.coslog.feature.elements

import android.content.Context
import android.net.Uri
import com.ifyezedev.coslog.core.data.BitmapHolder
import com.ifyezedev.coslog.core.extensions.loadOsGalleryBitmaps
import com.ifyezedev.coslog.core.extensions.mergeToBitmapHolders

class BitmapHolderCache {
    private val cache: MutableList<Uri> = mutableListOf()
    private val pathConverter = UriToBitmapGalleryPathConverterStandard()

    fun toBitmapHolders(context: Context) : List<BitmapHolder> {
        val bitmaps = context.contentResolver.loadOsGalleryBitmaps(cache)
        val filePaths = pathConverter.toFilePaths(cache)
        return bitmaps.mergeToBitmapHolders(filePaths)
    }

    fun addAll(cache: List<Uri>) {
        this.cache.addAll(cache)
    }
}