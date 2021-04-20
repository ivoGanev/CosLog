package com.ifyezedev.coslog.core.common.usecase

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.ifyezedev.coslog.core.exception.Failure
import com.ifyezedev.coslog.core.extensions.mapToUri
import com.ifyezedev.coslog.core.functional.Either

/**
* This use case loads bitmaps from the provided as an argument content provider path's
* */
class LoadBitmapsFromAndroidGallery(private val context: Context) :
    UseCase<List<Bitmap>, List<Uri>>() {

    override suspend fun run(params: List<Uri>): Either<List<Bitmap>, Failure> {
        val outBitmaps = mutableListOf<Bitmap>()

        params.forEach {
            context.contentResolver.openInputStream(it).use { stream ->
                outBitmaps.add(BitmapFactory.decodeStream(stream))
            }
        }
        return Either.Success(outBitmaps)
    }
}


