package com.tde.framework.widget.recyclerview.footer

/**
 * 可自行根据项目需求自定义加载更多的UI样式
 * <p>
 * Date: 2021-01-14
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
interface FooterProvider {

    /**
     * 创建加载中样式
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @return IFooter
     */
    fun createLoadingFooter(): IFooter = DefaultLoadingFooter()

    /**
     * 创建无更多样式
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @return IFooter
     */
    fun createNoMoreFooter(): IFooter = DefaultNoMoreFooter()

    /**
     * 创建加载失败样式
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @return IFooter
     */
    fun createLoadFailedFooter(): IFooter = DefaultFailedFooter()

}

object FooterUtils {

    private var footerProvider: FooterProvider? = null

    fun createFooterProvider(footerProvider: FooterProvider) {
        this.footerProvider = footerProvider
    }

    fun getFooterProvider(): FooterProvider {
        return footerProvider ?: object : FooterProvider {}
    }

}