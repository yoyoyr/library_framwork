package com.tde.framework.utils

import android.app.Application
import android.content.Context
import java.lang.RuntimeException

/**
 * 全局上下文对象工具类
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
object ContextUtils {

    private var context: Context? = null

    /**
     * 获取全局上下文
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-29
     * @return Application
     */
    fun getApplicationContext(): Context {
        if (context == null) {
            throw RuntimeException("请先初始化全局上下文对象")
        }
        return context!!
    }

    /**
     * 设置全局上下文对象
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-29
     * @param context Context
     */
    fun setApplicationContext(context: Context) {
        if (context is Application) {
            this.context = context
        } else {
            throw RuntimeException("请使用全局application上下文对象")
        }
    }


}