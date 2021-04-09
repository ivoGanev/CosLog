package com.ifyezedev.coslog.core.extensions

import android.graphics.Bitmap
import android.net.Uri
import com.ifyezedev.coslog.core.data.BitmapHolder
import java.lang.IndexOutOfBoundsException

fun List<Bitmap>.mergeToBitmapHolders(
    filePaths: List<String>
): List<BitmapHolder> {
    val result: MutableList<BitmapHolder> = mutableListOf()
    if (this.size != filePaths.size)
        throw IndexOutOfBoundsException("Lists should be equal.")

    for (i in this.indices)
        result.add(BitmapHolder(this[i], filePaths[i]))

    return result
}