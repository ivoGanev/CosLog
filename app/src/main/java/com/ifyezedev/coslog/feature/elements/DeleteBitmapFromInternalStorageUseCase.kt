package com.ifyezedev.coslog.feature.elements

import android.content.Context
import com.ifyezedev.coslog.core.data.BitmapHolder
import java.io.File

class DeleteBitmapFromInternalStorageUseCase {
     fun invoke(bitmapHolders: List<BitmapHolder>) {
        val deletePaths = bitmapHolders.filter { holder -> holder.filePath != null }
        deletePaths.forEach {
            val file = File(it.filePath)
            file.delete()
            println(it.filePath)
        }
    }
}