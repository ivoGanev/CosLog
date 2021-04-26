package com.ifyezedev.coslog.core.common.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import com.ifyezedev.coslog.core.exception.Failure
import com.ifyezedev.coslog.core.functional.Either
import java.io.File


/**
* This use case loads bitmaps from the provided as an argument content provider path's
* */
class LoadBitmapsFromAndroidGallery(private val context: Context) :
    UseCase<List<Bitmap>, List<Uri>>() {

    override suspend fun run(params: List<Uri>): Either<List<Bitmap>, Failure> {
        val outBitmaps = mutableListOf<Bitmap>()

        params.forEach {
            context.contentResolver.openInputStream(it).use { stream ->
                val bitmap = BitmapFactory.decodeStream(stream)
                outBitmaps.add(bitmap)
            }
        }
        return Either.Success(outBitmaps)
    }
}

