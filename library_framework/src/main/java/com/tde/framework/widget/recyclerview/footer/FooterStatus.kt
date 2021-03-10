package com.tde.framework.widget.recyclerview.footer

import androidx.annotation.IntDef
import com.tde.framework.widget.recyclerview.footer.FooterStatus.Companion.STATUS_LOADING
import com.tde.framework.widget.recyclerview.footer.FooterStatus.Companion.STATUS_NO_MORE
import com.tde.framework.widget.recyclerview.footer.FooterStatus.Companion.STATUS_NONE

/**
 * 加载更多枚举
 * <p>
 * Date: 2021-01-14
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
@IntDef(STATUS_LOADING, STATUS_NO_MORE, STATUS_NONE)
@Retention(AnnotationRetention.SOURCE)
annotation class FooterStatus {

    companion object {
        /**
         * 去掉所有加载状态
         */
        const val STATUS_NONE = 0

        /**
         * 显示正在加载中状态
         */
        const val STATUS_LOADING = 1

        /**
         * 显示没有更多数据加载的状态
         */
        const val STATUS_NO_MORE = 2

        /**
         * 加载失败重试
         */
        const val STATUS_FAILED = 3
    }

}