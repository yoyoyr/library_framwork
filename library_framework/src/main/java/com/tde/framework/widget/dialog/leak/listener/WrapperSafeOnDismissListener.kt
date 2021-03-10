package com.tde.framework.widget.dialog.leak.listener

import android.content.DialogInterface
import java.lang.ref.WeakReference

/**
 * 弱引用的OnDismissListener  防止内存泄漏
 * <p>
 * Date: 2020-12-28
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
class WrapperSafeOnDismissListener(listener: DialogInterface.OnDismissListener?) : DialogInterface.OnDismissListener {

    private var mListener: WeakReference<DialogInterface.OnDismissListener> = WeakReference(listener)
    override fun onDismiss(dialog: DialogInterface?) {
        mListener.get()?.onDismiss(dialog)
    }

}