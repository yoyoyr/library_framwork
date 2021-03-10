package com.tde.framework.extensions

import com.tde.framework.utils.ResourceUtils

/**
 * 资源扩展类
 * <p>
 * Date: 2020-12-16
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */

/**
 * Float 转dp   返回值为float类型
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Float
 * @return Float
 */
fun Float.dp(): Float {
    return ResourceUtils.dp2px(this)
}

/**
 * int 类型转dp  返回值为float类型
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Int
 * @return Float
 */
fun Int.dp() = toFloat().dp()

/**
 * Float 转dp   返回值为int类型
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Float
 * @return Int
 */
fun Float.idp() = dp().toInt()

/**
 * int 类型转dp  返回值为int类型
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Int
 * @return Int
 */
fun Int.idp() = dp().toInt()

/**
 * Float 转sp   返回值为float类型
 * eg. R.dimen.sp11.sp()
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Float
 * @return Float
 */
fun Float.sp(): Float {
    return ResourceUtils.dp2px(this)
}

/**
 * Float 转sp   返回值为float类型
 * eg. R.dimen.sp11.sp()
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Float
 * @return Float
 */
fun Float.isp(): Int {
    return ResourceUtils.dp2px(this).toInt()
}

/**
 * int 转sp   返回值为float类型
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Float
 * @return Float
 */
fun Int.sp() = toFloat().sp()


/**
 * int 转sp   返回值为float类型
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Float
 * @return Float
 */
fun Int.isp() = toFloat().isp()

/**
 * int 转color
 * eg. R.color.color_FFFFFF.color()
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Int
 * @return Int
 */
fun Int.color() = ResourceUtils.getColorResource(this)

/**
 * int 转string
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Int
 * @return String
 */
fun Int.string(): String = ResourceUtils.getStringResource(this)

/**
 * int 转string  带占位符的字符串资源
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Int
 * @return String
 */
fun Int.string(vararg args: Any?): String = ResourceUtils.getStringResource(this).format(*args)

/**
 * 图片资源获取
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Int
 * @return Drawable?
 */
fun Int.drawable() = ResourceUtils.getDrawable(this)