package com.tde.framework.utils

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.tde.framework.base.BaseApplication

/**
 * 资源工具类
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
object ResourceUtils {

    /**
     * 获取资源字符串
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-16
     * @param resId Int
     * @return String
     */
    fun getStringResource(@StringRes resId: Int): String {
        return BaseApplication.application.resources.getString(resId)
    }

    /**
     * 获取资源字符串
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-16
     * @param resId Int
     * @return String
     */
    fun getStringResource(@StringRes resId: Int,vararg args: Any?): String {
        return BaseApplication.application.resources.getString(resId).format(*args)
    }
    

    /**
     * 获取boolean资源
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-16
     * @param resId Int
     * @return Boolean
     */
    fun getBooleanResource(@BoolRes resId: Int): Boolean {
        return BaseApplication.application.resources.getBoolean(resId)
    }

    /**
     * 获取颜色值方法
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-16
     * @param resId Int
     * @return Int
     */
    fun getColorResource(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(BaseApplication.application, resId)
    }

    /**
     * 获取字符串数组
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-16
     * @param resId Int
     * @return Array<String?>?
     */
    fun getStringResourceArray(@ArrayRes resId: Int): Array<String?>? {
        return BaseApplication.application.resources.getStringArray(resId)
    }

    /**
     * 获取图片资源
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-16
     * @param resId Int
     * @return Drawable?
     */
    fun getDrawable(@DrawableRes resId: Int): Drawable? {
        return ContextCompat.getDrawable(BaseApplication.application, resId)
    }

    /**
     * sp 转 px
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-16
     * @param spVal Float
     * @return Float
     */
    @JvmStatic
    fun sp2px(spVal: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, Resources.getSystem().displayMetrics)

    /**
     * dp 转 px
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-16
     * @param dpVal Float
     * @return Int
     */
    @JvmStatic
    fun dp2px(dpVal: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, Resources.getSystem().displayMetrics) + 0.5f

    /**
     * px 转 dp
     * <p>
     * Author: ligangtang
     * Date: 2021-01-07
     * @param dpVal Float
     * @return Int
     */
    @JvmStatic
    fun px2dp(pxValue: Float): Int {
        val scale: Float = BaseApplication.application.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
    

    /**
     * 根据颜色及圆角创建ShapeDrawable
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-16
     * @return Drawable
     */
    fun createRoundDrawable(color: Int, l: Float = 0f, t: Float = 0f, r: Float = 0f, b: Float = 0f): Drawable {
        val arrayOf = floatArrayOf(l, l, t, t, r, r, b, b)
        val drawable = ShapeDrawable(RoundRectShape(arrayOf, null, null))
        drawable.paint.color = color
        return drawable
    }
    

}