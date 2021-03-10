package com.tde.framework.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * 当Fragment/Activity resume时执行，只会执行一次
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-24
 * @receiver LifecycleOwner
 * @param block Function0<Unit>
 */
fun LifecycleOwner.whenResumed(block: () -> Unit) {
    lifecycle.whenStateAtLeast(Lifecycle.State.RESUMED, block)
}

/**
 *  当Fragment/Activity start时执行，只会执行一次
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-24
 * @receiver LifecycleOwner
 * @param block Function0<Unit>
 */
fun LifecycleOwner.whenStarted(block: () -> Unit) {
    lifecycle.whenStateAtLeast(Lifecycle.State.STARTED, block)
}

/**
 * 当Fragment/Activity create时执行，只会执行一次
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-24
 * @receiver LifecycleOwner
 * @param block Function0<Unit>
 */
fun LifecycleOwner.whenCreate(block: () -> Unit) {
    lifecycle.whenStateAtLeast(Lifecycle.State.CREATED, block)
}

fun Lifecycle.whenStateAtLeast(minState: Lifecycle.State, block: () -> Unit) {
    LifecycleController(this, minState, block)
}

internal class LifecycleController(val lifecycle: Lifecycle,
                                   minState: Lifecycle.State,
                                   block: () -> Unit) {

    private val observer = LifecycleEventObserver { source, _ ->
        if (source.lifecycle.currentState == minState) {
            block()
            handleDestroy()
        } else if (source.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            handleDestroy()
        }
    }

    init {
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            handleDestroy()
        } else {
            lifecycle.addObserver(observer)
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun handleDestroy() {
        lifecycle.removeObserver(observer)
    }
}