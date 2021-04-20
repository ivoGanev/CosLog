package com.ifyezedev.coslog.core.functional

/**
 * Represents a value of one of two possible types.
 * Instances of [Either] are either an instance of [Left] or [Right].
 */
sealed class Either<out Left, out Right> {

    /** * Represents the left side of [Either] class which by convention is a "Result". */
    data class Success<out Left>(val data: Left) : Either<Left, Nothing>()

    /** * Represents the right side of [Either] class which by convention is a "Failure". */
    data class Failure<out Right>(val failure: Right) : Either<Nothing, Right>()

    val isSuccess get() = this is Success

    val isFailure get() = this is Failure

    /**
     * Creates a Left type.
     * @see Left
     */
    fun <Left> result(data: Left) = Success(data)

    /**
     * Creates a Failure type.
     * @see Right
     */
    fun <Right> failure(failure: Right) = Failure(failure)
}

fun <Left, Right> Either<Left, Right>.onSuccess(fn: (Left) -> Unit) {
    if (this is Either.Success) {
        fn(data)
    }
}

fun <Left, Right> Either<Left, Right>.onFailure(fn: (Right) -> Unit) {
    if (this is Either.Failure) {
        fn(failure)
    }
}