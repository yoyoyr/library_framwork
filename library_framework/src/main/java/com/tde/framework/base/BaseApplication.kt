package com.tde.framework.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.tde.framework.Switcher
import com.tde.framework.cache.CacheStore
import com.tde.framework.exception.TDEExceptionHandler
import com.tde.framework.init.InitManager
import com.tde.framework.net.TokenInterceptor
import com.tde.framework.toast.ToastUtils
import com.tde.framework.base.stack.ActivityStack
import com.tde.framework.utils.ContextUtils
import com.tde.framework.utils.LoggerUtils
import com.tde.network.core.log.Level
import com.tde.network.core.log.LoggingInterceptor
import com.tde.network.core.NedNetworkManager
import com.wanjian.cockroach.Cockroach
import okhttp3.Interceptor


open class BaseApplication : Application() {


    companion object {
        lateinit var application: Application
        var startTime = 0L
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        ToastUtils.init(application)
        ContextUtils.setApplicationContext(this)
        initLifeCycleCallback()
        //初始化异常捕获
        initCockroach()
        //神策依赖缓存
        CacheStore.init(application)

        InitManager.addMustTask("NedNetwork") {
            NedNetworkManager
                .addInterceptors(getInterceptors())
                .addLogInterceptor(
                    LoggingInterceptor.Builder().setLevel(Level.BASIC)
                        .loggable(true)
                        .request("Request")
                        .response("Response")
                        .build()
                )
                .addInterceptor(TokenInterceptor())
        }
    }


    override fun attachBaseContext(base: Context?) {
        startTime = System.currentTimeMillis()
        super.attachBaseContext(base)
        ContextUtils.setApplicationContext(this)
    }


    /**
     * 初始化异常崩溃sdk ,开发环境不进行初始化，避免开发时看不到崩溃日志
     * Author: zhuanhongzhan
     * Date: 2020-10-13
     */
    private fun initCockroach() {
        if (Switcher.COCKROACH) {
            Cockroach.install(this, TDEExceptionHandler())
        }
    }

    open fun getInterceptors(): List<Interceptor> {
        return emptyList()
    }

    /**
     *
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     */
    @Synchronized
    private fun initLifeCycleCallback() {
        this.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ActivityStack.addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                //..
            }

            override fun onActivityResumed(activity: Activity) {
                //..
            }

            override fun onActivityPaused(activity: Activity) {
                //..
            }

            override fun onActivityStopped(activity: Activity) {
                //..
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                //..
            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityStack.removeActivity(activity)
            }

        })

    }

}