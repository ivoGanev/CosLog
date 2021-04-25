package com.ifyezedev.coslog.core.common.usecase

import com.ifyezedev.coslog.core.exception.Failure
import com.ifyezedev.coslog.core.exception.Failure.*
import com.ifyezedev.coslog.core.functional.Either
import java.io.File

/**
 * This use case will delete any file from the internal storage with the provided
 * params path.
 *
 * params: the file to delete
 * */
class DeleteFilesFromInternalStorage : UseCase<Boolean, String>() {
    override suspend fun run(params: String): Either<Boolean, Failure> {
        val file = File(params)
        val fileIsDeleted: Boolean

        try {
            fileIsDeleted = file.delete()
        }
        catch (ex: SecurityException) {
            return Either.Failure(IOError(ex.message!!))
        }

        return Either.Success(fileIsDeleted)
    }
}