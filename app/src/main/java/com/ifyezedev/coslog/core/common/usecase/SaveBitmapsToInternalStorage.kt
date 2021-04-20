package com.ifyezedev.coslog.core.common.usecase

import android.graphics.Bitmap
import com.ifyezedev.coslog.core.exception.Failure
import com.ifyezedev.coslog.core.functional.Either
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class SaveBitmapsToInternalStorage : UseCase<Boolean, List<Pair<String, Bitmap>>>()  {
    override suspend fun run(params: List<Pair<String, Bitmap>>): Either<Boolean, Failure> {
        params.forEach { path ->
            //println(tag + bitmapHolder.filePath)
            try {
                FileOutputStream(File(path.first)).use { stream ->
                    path.second.compress(Bitmap.CompressFormat.PNG, 90, stream)
                }
            }
            catch(ex: FileNotFoundException) {
                return Either.Failure(Failure.IOError(ex.message!!))
            }
        }

        return Either.Success(true)
    }
}