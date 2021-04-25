package com.ifyezedev.coslog.core.common.usecase

import android.graphics.Bitmap
import com.ifyezedev.coslog.core.exception.Failure
import com.ifyezedev.coslog.core.functional.Either
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class SaveBitmapsToInternalStorage : UseCase<List<String>, List<Pair<String, Bitmap>>>()  {
    override suspend fun run(params: List<Pair<String, Bitmap>>): Either<List<String>, Failure> {
        val paths = mutableListOf<String>()

        params.forEach { bitmapPathPair ->
            try {
                FileOutputStream(File(bitmapPathPair.first)).use { stream ->
                    bitmapPathPair.second.compress(Bitmap.CompressFormat.PNG, 90, stream)
                }
            }
            catch(ex: FileNotFoundException) {
                return Either.Failure(Failure.IOError(ex.message!!))
            }
            paths.add(bitmapPathPair.first)
        }

        return Either.Success(paths)
    }
}