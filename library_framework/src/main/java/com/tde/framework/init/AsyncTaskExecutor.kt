package com.tde.framework.init

import java.util.concurrent.*

object AsyncTaskExecutor {

    private var executor = Executors.newCachedThreadPool()

    fun addTask(name: String, callback: () -> Unit): AsyncTaskExecutor {
        executor.execute(AsyncTask(name, callback))
        return this
    }

}