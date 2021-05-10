package com.ifyezedev.coslog.core.common.usecase

import com.ifyezedev.coslog.core.common.usecase.core.UseCase
import com.ifyezedev.coslog.core.exception.Failure
import com.ifyezedev.coslog.core.exception.Failure.*
import com.ifyezedev.coslog.core.functional.Either
import java.io.File

/**
 * This use case will delete any file from the internal storage with the provided
 * params path.
 *
 * @param params list of internal storage full paths e.g. /data/data/image-gallery/file1.png.
 *
 * If any of the files fails to open this method will return false, else true.
 * */
class DeleteFilesFromInternalStorage : UseCase<UseCase.None, List<String>>() {
    override suspend fun run(params: List<String>): Either<None, Failure> {
        println("here")
        for(i in params.indices) {
            val file = File(params[i])
            try {
                file.delete()
            } catch (ex: SecurityException) {
                return Either.Failure(IOError(ex.message!!))
            }
        }
        return Either.Success(None())
    }
}