package com.tde.framework.widget.view

import android.view.View

/**
 * 防止快速重复点击
 * <p>
 * Date: 2021-02-08
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * @property lastClickTime Long
 *
 * Author: zhuanghongzhan
 */
abstract class OnMultiClickListener : View.OnClickListener {

    companion object {
        // 两次点击按钮之间的点击间隔不能少于1000毫秒
        private const val MIN_CLICK_DELAY_TIME = 1000

    }

    private var lastClickTime: Long = 0L


    abstract fun onMultiClick(v: View?)

    override fun onClick(v: View?) {
        val curClickTime = System.currentTimeMillis()
        if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime
            onMultiClick(v)
        }
    }
}