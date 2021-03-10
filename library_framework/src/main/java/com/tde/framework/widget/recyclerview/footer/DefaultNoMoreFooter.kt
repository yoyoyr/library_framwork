package com.tde.framework.widget.recyclerview.footer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tde.framework.R

/**
 * 默认无更多数据样式
 * <p>
 * Date: 2021-01-14
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
class DefaultNoMoreFooter : IFooter {

    override fun inflate(context: Context, viewGroup: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.list_footer_no_more, viewGroup, false)
    }
}