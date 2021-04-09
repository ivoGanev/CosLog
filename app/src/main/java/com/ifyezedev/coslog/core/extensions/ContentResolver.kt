package com.ifyezedev.coslog.core.extensions

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

fun ContentResolver.loadOsGalleryBitmap(contentProviderUri: Uri): Bitmap {
    val bitmapStream = this.openInputStream(contentProviderUri)
    return BitmapFactory.decodeStream(bitmapStream)
}

fun ContentResolver.loadOsGalleryBitmaps(uris: List<Uri>): MutableList<Bitmap> {
    val result = mutableListOf<Bitmap>()
    uris.forEach { uri ->
        result.add(this.loadOsGalleryBitmap(uri))
    }
    return result
}
