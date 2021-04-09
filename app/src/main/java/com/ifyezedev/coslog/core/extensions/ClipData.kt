package com.ifyezedev.coslog.core.extensions

import android.content.ClipData
import android.net.Uri

/**
 * Project the uri data contained within the [ClipData] as a List
 * */
fun ClipData.mapToUri(): List<Uri> {
    val result = mutableListOf<Uri>()
    for (i in 0 until this.itemCount) {
        result.add(this.getItemAt(i).uri)
    }
    return result
}