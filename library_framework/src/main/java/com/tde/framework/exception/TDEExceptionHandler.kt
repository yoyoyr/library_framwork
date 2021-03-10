package com.tde.framework.exception

import com.tde.framework.BuildConfig
import com.tde.framework.utils.LoggerUtils
import com.tencent.bugly.crashreport.CrashReport
import com.wanjian.cockroach.ExceptionHandler

/**
 * 异常拦截处理，避免崩溃，并上传bugly
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
class TDEExceptionHandler : ExceptionHandler() {


    /**
     * 子线程抛出异常时始终调用该方法。主线程只有第一次抛出异常时才会调用该方法，该方法中到的throwable都会上报到bugly。以后主线程的异常只调用 [onBandageExceptionHappened]
     *
     * @param thread
     * @param throwable
     */
    override fun onUncaughtExceptionHappened(thread: Thread?, throwable: Throwable?) {
        throwable?.printStackTrace()
        if(throwable!=null){
            LoggerUtils.LOGE(throwable)
        }
        if (BuildConfig.DEBUG) {
            throwable?.printStackTrace()
        }
        CrashReport.postCatchedException(throwable)

    }

    /**
     * 当原本导致app崩溃的主线程异常发生后，主线程再次抛出导致app崩溃异常时会调用该方法。（自己try catch住的异常不会导致app崩溃）
     * （该方法中到的throwable不会上报到bugly，也无需上报到bugly，因为本次异常可能是由于第一次主线程异常时app没有崩溃掉才发生的，只要修复了bug就不会发生该异常了)
     *
     * @param throwable 主线程的异常
     */
    override fun onBandageExceptionHappened(throwable: Throwable?) {
        throwable?.printStackTrace()
        if(throwable!=null){
            LoggerUtils.LOGE(throwable)
        }
        if (BuildConfig.DEBUG) {
            throwable?.printStackTrace()
        }
        CrashReport.postCatchedException(throwable)
    }

    override fun onEnterSafeMode() {
        //这边在debug模式下应该跳转到对应的收集页面
    }

    override fun onMayBeBlackScreen(e: Throwable?) {
        super.onMayBeBlackScreen(e)
    }
}