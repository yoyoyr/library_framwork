package com.tde.framework.extensions.basetype

import android.graphics.Color


fun String.toColor() = Color.parseColor(this)


fun String.getFileSize(): String {
    if (this.endsWith("KB") || this.endsWith("MB") || this.endsWith("GB") || this.endsWith("TB")) {
        return this.substring(0, length - 2)
    }
    return "0.0"
}

/**
 * 获取文件大小的单位
 * <p>
 * Author: zhuanghongzhan
 * Date: 2021-01-06
 * @receiver String
 * @return String
 */
fun String.getFileUnit(): String {
    if (this.endsWith("KB") || this.endsWith("MB") || this.endsWith("GB") || this.endsWith("TB")) {
        return this.substring(length - 2)
    }
    return "KB"
}
