package com.tde.framework.extensions.basetype

import java.math.BigDecimal


/**
 * double保留几位小数
 * <p>
 * Author: yangrong
 * Date: 2021-03-09
 * @receiver Double
 * @param len Int
 * @return Double
 */
fun Double.limitDigits(len: Int): Double {
    return BigDecimal(this).setScale(len, BigDecimal.ROUND_HALF_UP)?.run {
        toDouble()
    } ?: this
}
