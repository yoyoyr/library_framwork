package com.tde.framework.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.tde.framework.utils.LoggerUtils.LOGE

/**
 *
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
object SystemSettingsUtil {

    /**
     * 跳转到系统权限设置界面
     */
    fun gotoPermissionSetting(context: Context?) {
        context?.let {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri = Uri.fromParts("package", it.packageName, null)
            intent.data = uri
            try {
                it.startActivity(intent)
            } catch (e: Exception) {
                LOGE(e)
            }
        }
    }
}