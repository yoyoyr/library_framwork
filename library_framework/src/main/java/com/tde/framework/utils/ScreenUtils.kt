package com.tde.framework.utils

import android.content.Context
import android.graphics.Point
import android.os.Build

import android.view.WindowManager


/**
 *
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
object ScreenUtils {

    /**
     * 获取屏幕宽度
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-29
     * @return Int
     */
    fun getScreenWidth(): Int {
        val wm = ContextUtils.getApplicationContext().getSystemService(Context.WINDOW_SERVICE) as? WindowManager
                ?: return -1
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.x
    }


    /**
     * 获取屏幕高度
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-29
     * @return Int
     */
    fun getScreenHeight(): Int {
        val wm = ContextUtils.getApplicationContext().getSystemService(Context.WINDOW_SERVICE) as? WindowManager
                ?: return -1
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.y
    }

    fun getStatusBarHeight(): Int {
        val context = ContextUtils.getApplicationContext()
        var result = 0
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}