package com.ifyezedev.coslog.core.common.usecase

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import com.ifyezedev.coslog.core.exception.Failure
import com.ifyezedev.coslog.core.functional.Either
import com.ifyezedev.coslog.feature.elements.internal.FilePathProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class SaveBitmapsToInternalStorage(
    private val filePathProvider: FilePathProvider,
) : UseCase<List<String>, List<Pair<Uri, Bitmap>>>() {

    override suspend fun run(params: List<Pair<Uri, Bitmap>>): Either<List<String>, Failure> {
        val paths = mutableListOf<String>()

        // make sure the image directory exists
        val file = File(filePathProvider.getDefaultImageDirectory())
        if (!file.exists())
            file.mkdirs()

        params.forEach { bitmapPathPair ->
            try {
                val path = filePathProvider.generateFilePath(bitmapPathPair.first)

                FileOutputStream(File(path)).use { stream ->
                    bitmapPathPair.second.compress(Bitmap.CompressFormat.PNG, 90, stream)
                }

                paths.add(path)
            } catch (ex: FileNotFoundException) {
                return Either.Failure(Failure.IOError(ex.message!!))
            }

        }

        return Either.Success(paths)
    }
}