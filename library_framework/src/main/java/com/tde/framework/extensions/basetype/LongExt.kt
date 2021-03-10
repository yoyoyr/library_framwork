package com.tde.framework.extensions.basetype

import android.os.Build
import com.tde.framework.utils.LoggerUtils
import java.math.BigDecimal
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


//====================================================毫秒转换相关

fun Long.getBetweenHour(): Long {
    try {
        return (System.currentTimeMillis() - this) / 1000 / 60 / 60
    } catch (ex: Exception) {
        LoggerUtils.LOGW(ex)
        return 0
    }
}

/**
 * 毫秒转秒
 * <p>
 * Author: yangrong
 * Date: 2021-02-24
 * @receiver Long
 * @return Long
 */
fun Long.millisToSecond() = this / 1000

/**
 * 毫秒转分钟
 * <p>
 * Author: yangrong
 * Date: 2021-02-24
 * @receiver Long
 * @return Long
 */
fun Long.millisToMinute() = this / (1000 * 60)


/**
 *
 * <p> 毫秒转日期
 * Author: yangrong
 * Date: 2021-01-11
 * @param size Long
 * @return String
 */
fun Long.toDateTime() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    SimpleDateFormat("YY-MM-dd hh:mm:ss").format(this)
} else {
    DateFormat.getInstance().format(Date(this))
}

/**
 *
 * <p> 毫秒转当前小时
 * Author: yangrong
 * Date: 2021-01-11
 * @param size Long
 * @return String
 */
fun Long.toHourTime() = SimpleDateFormat("HH").format(this)


/**
 * 两个时间段相差几天
 * <p>
 * Author: yangrong
 * Date: 2021-02-08
 * @receiver Long
 * @param time Long
 * @return Int
 */
fun Long.betweenDay(time: Long): Int {
    val calendar = Calendar.getInstance(Locale.CHINA)
    val now = calendar.get(Calendar.DAY_OF_YEAR)
    calendar.setTime(Date(time))
    val save = calendar.get(Calendar.DAY_OF_YEAR) //传入time对应的时间
    LoggerUtils.LOGV("now $now , time = $save")
    return now - save
}




//====================================================byte单位转换

fun Long.toSize(): String {
    val format = formatFileUnit(1)
    val size = format.getFileSize()
    return "$size"
}

fun Long.toUnit(): String {
    val format = formatFileUnit(1)
    val sizeUnit = format.getFileUnit()
    return "$sizeUnit"
}

/**
 * 格式化为文件大小数值
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-31
 * @receiver Long
 */
fun Long.formatFileUnit(scale: Int = 2): String {
    val size = this.toDouble()
    val kiloByte = size / 1024
    if (kiloByte < 1) {
        return "0.0KB"
    }

    val megaByte = kiloByte / 1024
    if (megaByte < 1) {
        val result1 = BigDecimal(kiloByte)
        return result1.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString().toString() + "KB"
    }

    val gigaByte = megaByte / 1024
    if (gigaByte < 1) {
        val result2 = BigDecimal(megaByte)
        return result2.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
    }

    val teraBytes = gigaByte / 1024
    if (teraBytes < 1) {
        val result3 = BigDecimal(gigaByte)
        return result3.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
    }

    val result4 = BigDecimal(teraBytes)
    return (result4.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB")
}
