package com.tde.framework.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * 生命周期接口
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
interface IBaseLifecycle : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?){}

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(){}

}