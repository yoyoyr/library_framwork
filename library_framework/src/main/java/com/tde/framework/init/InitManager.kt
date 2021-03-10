package com.tde.framework.init

import android.os.Looper
import android.os.MessageQueue
import com.tde.framework.utils.LoggerUtils
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object InitManager {

    private var tasks = ArrayList<InitRunnable>(10)
    private var taskCount = 0
    private var latch: CountDownLatch? = null
    private var executor = ThreadPoolExecutor(
        0, 5, 1, TimeUnit.SECONDS,
        ArrayBlockingQueue(20)
    )

    //主线程将阻塞直到任务结束
    fun addMustTask(name: String, callback: () -> Unit): InitManager {
        executor.submit(AsyncTask(name, callback))
        taskCount++
        return this
    }


    //任务是否结束不影响主线程
    fun addTask(name: String, callback: () -> Unit): InitManager {
        tasks.add(AsyncTask(name, callback))
        return this
    }

    //主线程空闲时执行任务
    fun addIdleTask(name: String, callback: () -> Unit = {}): InitManager {
        Looper.myQueue().addIdleHandler(IdleTask(name, callback))
        return this
    }

    fun init() {
        LoggerUtils.LOGV("init task count = $taskCount")
        if (taskCount <= 0) {
            return
        }

        latch = CountDownLatch(taskCount)
        if (tasks.size > 0) {
            for (runable in tasks) {
                executor.submit(runable)
            }
        }
        taskCount = 0
        latch?.await()
    }

    fun taskFinish() {
        latch?.countDown()
    }


    abstract class IdleHandler(val name: String) : MessageQueue.IdleHandler {
        override fun queueIdle(): Boolean {
            val start = System.currentTimeMillis()
            idle()
            LoggerUtils.LOGV("init $name coast ${System.currentTimeMillis() - start}")
            return false
        }

        abstract fun idle()
    }
}