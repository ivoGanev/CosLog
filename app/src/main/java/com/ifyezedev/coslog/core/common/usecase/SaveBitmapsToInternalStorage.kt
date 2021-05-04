package com.ifyezedev.coslog.core.common.usecase

import android.graphics.Bitmap
import com.ifyezedev.coslog.core.common.usecase.core.UseCase
import com.ifyezedev.coslog.core.exception.Failure
import com.ifyezedev.coslog.core.functional.Either
import com.ifyezedev.coslog.core.common.ImageFilePathProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class SaveBitmapsToInternalStorage(
    private val imageFilePathProvider: ImageFilePathProvider,
) : UseCase<List<String>, List<Pair<String, Bitmap>>>() {


    override suspend fun run(params: List<Pair<String, Bitmap>>): Either<List<String>, Failure> {
        val paths = mutableListOf<String>()
        // make sure the image directory exists
        val file = File(imageFilePathProvider.getDefaultImageDirectoryPath())
        if (!file.exists())
            file.mkdirs()

        params.forEach { bitmapPathPair ->
            try {
                //val path = imageFilePathProvider.toInternalStorageFilePath(bitmapPathPair.first)
                val path = bitmapPathPair.first
                val newFile = File(path)
                paths.add(path)

                if (newFile.exists()) {
                    return@forEach
                }

                FileOutputStream(File(path)).use { stream ->
                    bitmapPathPair.second.compress(Bitmap.CompressFormat.PNG, 90, stream)
                }

            } catch (ex: FileNotFoundException) {
                return Either.Failure(Failure.IOError(ex.message!!))
            }

        }

        return Either.Success(paths)
    }
}