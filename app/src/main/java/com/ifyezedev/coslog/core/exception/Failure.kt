package com.ifyezedev.coslog.core.exception

/**
 * Base Class for handling errors/failures/exceptions.
 */
sealed class Failure {
    /** An input-output error is used when there is a problem with saving and
     * loading files */
    object IOError : Failure()
}
