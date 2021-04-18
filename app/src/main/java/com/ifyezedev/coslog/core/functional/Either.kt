package com.ifyezedev.coslog.core.functional

/**
 * Represents a value of one of two possible types.
 * Instances of [Either] are either an instance of [Left] or [Right].
 */
sealed class Either<out Left,out Right> {

    /** * Represents the left side of [Either] class which by convention is a "Result". */
    data class Result<out Left>(val data: Left): Either<Left, Nothing>()

    /** * Represents the right side of [Either] class which by convention is a "Failure". */
    data class Failure<out Right>(val data: Right): Either<Nothing, Right>()

    /**
     * Creates a Left type.
     * @see Left
     */
    fun <Left> result(a: Left) = Result(a)


    /**
     * Creates a Failure type.
     * @see Right
     */
    fun <Right> failure(b: Right) = Failure(b)
}