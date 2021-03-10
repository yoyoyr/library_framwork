package com.tde.framework.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

/**
 * 回调完成状态
 * <p>
 * Author: yangrong
 * Date: 2021-03-10
 * @receiver ViewModel
 * @param block SuspendFunction1<[@kotlin.ParameterName] CoroutineScope, Unit>
 * @param complete Function0<Unit>
 * @return Job
 */
fun ViewModel.completeLaunch(
    block: suspend (coroutineScope: CoroutineScope) -> Unit,
    complete: () -> Unit = { }
): Job {
    return viewModelScope.cancelLaunch(block, complete)

}


fun <T> CoroutineScope.completeAsync(
    block: suspend () -> T,
    complete: () -> Unit = { }
) {
//    val job = safeAsync(block)
//
//    return job

    val job = async {
        var result: T? = null
        try {
            result = block()
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        result
    }
    job.invokeOnCompletion {
        complete()
    }
    job
}

fun CoroutineScope.cancelLaunch(
    block: suspend (coroutineScope: CoroutineScope) -> Unit,
    complete: () -> Unit = { }
): Job {
    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }
    val job = launch(exceptionHandler) { block(this) }
    job.invokeOnCompletion {
        complete()
    }
    return job
}

/**
 * 可关闭的CoroutineScope
 * <p>
 * Date: 2020-12-11
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}