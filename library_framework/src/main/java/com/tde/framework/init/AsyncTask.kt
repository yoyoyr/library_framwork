package com.tde.framework.init

import com.tde.framework.utils.LoggerUtils
import java.lang.Exception

/**
 *
 * <p>
 * Author: yangrong
 * @description:
 * @date :2020/12/23 18:04
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
internal class AsyncTask(val n: String, val callback: () -> Unit) : InitRunnable(n) {
    override fun execute() {
        LoggerUtils.LOGV("do task $n")
        try {
            callback()
        } catch (ex: Exception) {
            LoggerUtils.LOGW(ex)
        }
    }
}