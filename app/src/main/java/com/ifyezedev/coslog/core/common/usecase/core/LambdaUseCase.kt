package com.ifyezedev.coslog.core.common.usecase.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class LambdaUseCase<ParamsA, ParamsB, Result> {
    operator fun invoke(scope: CoroutineScope, params: (ParamsA, ParamsB) -> Unit, onResult:  (Result) -> Unit = {}) {
        val job = scope.async(Dispatchers.IO) { run(params) }
        scope.launch(Dispatchers.Main) { onResult(job.await()) }
    }

    protected abstract suspend fun run(params: (ParamsA, ParamsB) -> Unit) : Result
}

