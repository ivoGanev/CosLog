package com.ifyezedev.coslog.core.common.usecase

import com.ifyezedev.coslog.core.exception.Failure
import com.ifyezedev.coslog.core.exception.Failure.*
import com.ifyezedev.coslog.core.functional.Either
import java.io.File

class DeleteBitmapsFromInternalStorage : UseCase<Boolean, String>() {

    override suspend fun run(params: String): Either<Boolean, Failure> {
        val file = File(params)
        val fileIsDeleted: Boolean

        try {
            fileIsDeleted = file.delete()
        }
        catch (ex: SecurityException) {
            return Either.Failure(IOError)
        }

        return Either.Result(fileIsDeleted)
    }
}