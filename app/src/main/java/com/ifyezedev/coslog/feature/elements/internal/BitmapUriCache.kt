package com.ifyezedev.coslog.feature.elements.internal

import android.content.Context
import android.net.Uri
import com.ifyezedev.coslog.core.data.BitmapHolder
import com.ifyezedev.coslog.core.extensions.loadOsGalleryBitmaps
import com.ifyezedev.coslog.core.extensions.mergeToBitmapHolders

class BitmapUriCache {
    val data: MutableList<Uri> = mutableListOf()
}