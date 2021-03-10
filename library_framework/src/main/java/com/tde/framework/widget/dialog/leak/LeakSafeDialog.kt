package com.tde.framework.widget.dialog.leak

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import com.tde.framework.widget.dialog.leak.listener.WrapperSafeOnCancelListener
import com.tde.framework.widget.dialog.leak.listener.WrapperSafeOnDismissListener
import com.tde.framework.widget.dialog.leak.listener.WrapperSafeOnShowListener

/**
 * 解决dialog泄漏问题
 * dialog的listener是用handler发送的，导致dialog被持有无法释放
 * 解决：包装一层listener，使用WeakReference持有
 * <p>
 * Date: 2020-12-28
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
open class LeakSafeDialog : Dialog {

    constructor(context: Context) : super(context)

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener)

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        super.setOnCancelListener(WrapperSafeOnCancelListener(listener))
    }

    override fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
        super.setOnShowListener(WrapperSafeOnShowListener(listener))
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(WrapperSafeOnDismissListener(listener))
    }
}