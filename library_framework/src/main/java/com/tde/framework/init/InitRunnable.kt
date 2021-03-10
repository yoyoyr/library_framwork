package com.tde.framework.init

import com.tde.framework.utils.LoggerUtils

internal abstract class InitRunnable(val name: String) : Runnable {
    override fun run() {
        val start = System.currentTimeMillis()
        execute()
        InitManager.taskFinish()
        LoggerUtils.LOGV("init $name coast ${System.currentTimeMillis() - start}")
    }

    abstract fun execute()
}
