@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.tde.framework.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.tde.framework.base.BaseApplication

/**
 * intent 扩展方法
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
class IntentExt

/**
 * 安全的打开intent，避免崩溃
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Context
 * @param intent Intent?
 * @param catchBlock Function1<[@kotlin.ParameterName] Throwable, Unit>
 */
inline fun Context.startSafely(intent: Intent?, catchBlock: (e: Throwable) -> Unit = { it.printStackTrace() }) {
    try {
        if (intent != null) startActivity(intent)
    } catch (e: Throwable) {
        catchBlock(e)
    }
}

/**
 * 判断是否可以跳转
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver Intent
 */
fun Intent.resolveIntent(context: Context = BaseApplication.application, checkPermission: Boolean = false): ResolveInfo? {
    val resolveInfo = context.packageManager.resolveActivity(this, PackageManager.MATCH_DEFAULT_ONLY)
    return if (checkPermission && resolveInfo?.activityInfo?.permission != null) null else resolveInfo
}

/**
 * 添加 [Intent.FLAG_ACTIVITY_NEW_TASK] flag 到 [Intent] 里面去
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @return Intent 返回设置flag之后的intent回去
 */
inline fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }





