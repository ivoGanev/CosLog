package com.ifyezedev.coslog.core.common.usecase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.ifyezedev.coslog.core.common.usecase.core.UseCase
import com.ifyezedev.coslog.core.exception.Failure
import com.ifyezedev.coslog.core.functional.Either
import com.ifyezedev.coslog.core.common.ImageFilePathProvider
import java.io.File
import java.io.FileInputStream
import java.lang.Exception

class LoadBitmapsFromInternalStorage(
    private val imageFilePathProvider: ImageFilePathProvider,
) : UseCase<List<Bitmap>, List<String>>() {
    // We are using the IODispatcher so suppressing the warning is fine
    @Suppress("BlockingMethodInNonBlockingContext")
    /**
     * Will try to load any number of bitmaps from the internal storage and return them as a result.
     * Note that you need to provide the full path, e.g. /data/data/images/myfile.png
     *
     * @params You can provide either: a single file path, a folder, or multiple files and folders.
     * */
    override suspend fun run(params: List<String>): Either<List<Bitmap>, Failure> {
        val outBitmaps = mutableListOf<Bitmap>()

        for (i in params.indices) {
            val file = File(params[i])

            val bitmapsFilesToOpen = if (file.isDirectory) {
                file.listFiles()
            } else {
                arrayOf(file)
            }

            bitmapsFilesToOpen?.let {
                try {
                    for (i in bitmapsFilesToOpen.indices) {
                        FileInputStream(bitmapsFilesToOpen[i]).use { stream ->
                            outBitmaps.add(BitmapFactory.decodeStream(stream))
                        }
                    }
                } catch (e: Exception) {
                    return Either.Failure(Failure.IOError(e.message!!))
                }
            }
        }
        return Either.Success(outBitmaps)
    }
}
