package com.tde.framework.widget.title

import com.tde.framework.R

/**
 * 设置和获取标题栏设置的工具类
 * <p>
 * Date: 2021-01-05
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
object GlobalTitleBarProvider {

    private var titleBarBuilder: TitleBarBuilder = TitleBarBuilder()
        .setTitleGravity(TitleBarBuilder.CENTER)
        .setTitleBarBackgroundColorRes(R.color.color_FFFFFF)
        .setTitleBarLeftBackIcon(R.mipmap.nav_back)


    fun setTitleBarBuilder(titleBarBuilder: TitleBarBuilder) {
        this.titleBarBuilder = titleBarBuilder
    }


    fun getTitleBarBuilder(): TitleBarBuilder {
        return titleBarBuilder
    }

}