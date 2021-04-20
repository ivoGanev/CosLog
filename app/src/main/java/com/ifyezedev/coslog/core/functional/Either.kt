package com.ifyezedev.coslog.core.functional

/**
 * Represents a value of one of two possibilities.
 * Instances of [Either] are either an instance of [Left] or [Right].
 * By convention we are using [Left] for [Success] and [Right] for [Failure]
 * Why use Either?
 *
 * @see <a href="https://stackoverflow.com/questions/10818427/is-either-the-equivalent-to-checked-exceptions#:~:text=The%20main%20difference%20between%20Either,with%20Either%20is%20always%20explicit.&text=Also%2C%20Either%20does%20not%20capture,be%20used%20for%20control%20flow">Stack-Overflow Either vs Exception</a>
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