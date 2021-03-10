package com.tde.framework.widget.recyclerview.footer

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * 加载更多样式接口
 * <p>
 * Date: 2021-01-14
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
interface IFooter {

    fun inflate(context: Context, viewGroup: ViewGroup): View

    fun onAttach() {
        // default do nothing
    }

    fun onDetached() {
        // default do nothing
    }

}