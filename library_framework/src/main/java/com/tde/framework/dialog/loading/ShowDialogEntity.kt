package com.tde.framework.dialog.loading

import android.content.DialogInterface

/**
 * 展示 loading dialog 所需参数
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * @param title String?
 * @param isCancelable Boolean
 * @param isCancelOutside Boolean
 * @param onCancelListener OnCancelListener?
 * @constructor
 *
 * Author: yangrong
 */
data class ShowDialogEntity(
    var title: String? = null,
    var isCancelable: Boolean,
    var isCancelOutside: Boolean,
    var onCancelListener: DialogInterface.OnCancelListener?
)