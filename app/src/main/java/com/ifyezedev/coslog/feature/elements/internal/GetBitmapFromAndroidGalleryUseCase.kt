package com.ifyezedev.coslog.feature.elements.internal

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.ifyezedev.coslog.core.data.BitmapHolder
import com.ifyezedev.coslog.core.extensions.loadOsGalleryBitmaps
import com.ifyezedev.coslog.core.extensions.mapToUri
import com.ifyezedev.coslog.core.extensions.mergeToBitmapHolders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 1. Gets a bitmap from a content provider gallery Uri.
 * 2. Converts the Uri to a custom file path.
 * 3. Provides a [BitmapHolder] and puts it inside the provided adapter.
 * */
internal class GetBitmapFromAndroidGalleryUseCase() :
    ManageGalleryUseCase() {
    private fun loadBitmaps(context: Context, uri: List<Uri>): List<Bitmap> {
        return context.contentResolver.loadOsGalleryBitmaps(uri)
    }

    /**
     * @param onResult returns a list of loaded bitmaps and their content provider Uri address.
     * */
    suspend fun invoke(
        context: Context, intent: Intent,
        onResult: (List<Bitmap>, List<Uri>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val tempUriCache: MutableList<Uri> = mutableListOf()

            intent.data?.let { uri -> tempUriCache.add(uri) }
            intent.clipData?.let { clipData -> tempUriCache.addAll(clipData.mapToUri()) }

            val bitmaps = loadBitmaps(context, tempUriCache)
            onResult(bitmaps, tempUriCache)
        }
    }

}

