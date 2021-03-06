package com.ifyezedev.coslog.core.common.usecase.core

import com.ifyezedev.coslog.core.exception.Failure
import com.ifyezedev.coslog.core.functional.Either
import kotlinx.coroutines.*

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means that any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */
abstract class UseCase<out ResultType, in Params> where ResultType : Any {

    protected abstract suspend fun run(params: Params): Either<ResultType, Failure>

    operator fun invoke(scope: CoroutineScope, params: Params, onResult: (Either<ResultType, Failure>) -> Unit = {}) {
        val job = scope.async(Dispatchers.IO) { run(params) }
        scope.launch(Dispatchers.Main) { onResult(job.await()) }
    }

    class None
}
