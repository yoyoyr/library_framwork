package com.tde.framework.widget.title

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.tde.framework.R
import com.tde.framework.extensions.color
import com.tde.framework.extensions.drawable

/**
 * 标题栏构建 用于处理不同app的不同样式，这个是全局通用设置，如果有特殊的定制化则在activity里面进行自定义处理
 * <p>
 * Date: 2021-01-05
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
class TitleBarBuilder {

    companion object {

        /**
         * 标题左对其
         */
        const val LEFT = 0

        /**
         * 标题居中对其
         */
        const val CENTER = 1
    }

    /**
     * 标题对其方式
     */
    private var mTitleGravity: Int = CENTER

    /**
     * 标题颜色
     */
    private var mTitleTextColor: Int = R.color.color_000000.color()

    /**
     * 标题是否加粗
     */
    private var mTitleTextBold: Boolean = true

    /**
     * 标题栏的背景颜色
     */
    private var mTitleBarBackgroundColor: Int = R.color.color_FFFFFF.color()

    /**
     * 标题栏左边的返回图标
     */
    private var mTitleBarLeftBackIcon: Drawable? = R.mipmap.nav_back.drawable()

    /**
     * 状态栏颜色
     */
    private var mStatusBarColor: Int = R.color.color_FFFFFF.color()

    /**
     * 点击标题返回
     */
    var clickTitleToBack = false

    /**
     * 设置标题栏的标题对其方式   目前只支持设置居中和居左
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @param gravity Int
     */
    fun setTitleGravity(gravity: Int): TitleBarBuilder {
        if (gravity in LEFT..CENTER) {
            this.mTitleGravity = gravity
        }
        return this
    }

    /**
     * 获取
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @return Int
     */
    fun getTitleGravity(): Int = mTitleGravity

    /**
     * 设置标题的颜色
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @param color Int
     * @return TitleBarBuilder
     */
    fun setTitleTextColor(@ColorInt color: Int): TitleBarBuilder {
        this.mTitleTextColor = color
        return this
    }

    /**
     * 设置标题颜色
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @param color Int
     * @return TitleBarBuilder
     */
    fun setTitleTextColorRes(@ColorRes color: Int): TitleBarBuilder {
        this.mTitleTextColor = color.color()
        return this
    }


    /**
     * 获取标题栏的颜色
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @return Int
     */
    fun getTitleTextColor(): Int = mTitleTextColor

    /**
     * 设置标题是否加粗，默认是加粗
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @param bold Boolean
     * @return TitleBarBuilder
     */
    fun setTitleTextBold(bold: Boolean): TitleBarBuilder {
        this.mTitleTextBold = bold
        return this
    }

    fun getTitleTextBold(): Boolean = mTitleTextBold

    /**
     * 设置标题栏的背景颜色
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @param color Int
     * @return TitleBarBuilder
     */
    fun setTitleBarBackgroundColor(@ColorInt color: Int): TitleBarBuilder {
        this.mTitleBarBackgroundColor = color
        return this
    }

    /**
     * 设置标题栏的背景颜色
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @param res Int
     * @return TitleBarBuilder
     */
    fun setTitleBarBackgroundColorRes(@ColorRes res: Int): TitleBarBuilder {
        this.mTitleBarBackgroundColor = res.color()
        return this
    }

    /**
     * 获取标题栏背景颜色
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @return Int
     */
    fun getTitleBarBackgroundColor(): Int = mTitleBarBackgroundColor

    /**
     * 设置标题栏左边返回键的图标
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @return TitleBarBuilder
     */
    fun setTitleBarLeftBackIcon(icon: Drawable): TitleBarBuilder {
        this.mTitleBarLeftBackIcon = icon
        return this
    }

    /**
     * 设置标题栏左边返回键的图标
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @return TitleBarBuilder
     */
    fun setTitleBarLeftBackIcon(iconRes: Int): TitleBarBuilder {
        this.mTitleBarLeftBackIcon = iconRes.drawable()
        return this
    }

    /**
     * 获取标题栏坐标的返回按钮
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @return Drawable?
     */
    fun getTitleBarLeftBackIcon(): Drawable? = mTitleBarLeftBackIcon


    /**
     * 设置标题栏左边返回键的图标
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @return TitleBarBuilder
     */
    fun setStatusBarColor(@ColorInt color: Int): TitleBarBuilder {
        this.mStatusBarColor = color
        return this
    }

    /**
     * 设置标题栏左边返回键的图标
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @return TitleBarBuilder
     */
    fun setStatusBarColorRes(@ColorRes colorRes: Int): TitleBarBuilder {
        this.mStatusBarColor = colorRes.color()
        return this
    }

    /**
     * 获取标题栏坐标的返回按钮
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @return Drawable?
     */
    fun getStatusBarColor(): Int = mStatusBarColor

    /**
     * 设置点击标题返回
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-02-09
     * @param back Boolean
     * @return TitleBarBuilder
     */
    fun setClickTitleToBack(back: Boolean): TitleBarBuilder {
        this.clickTitleToBack = back
        return this
    }


}