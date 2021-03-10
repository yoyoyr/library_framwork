package com.tde.framework.widget.dialog.leak.listener

import android.content.DialogInterface
import java.lang.ref.WeakReference

/**
 * 弱引用的OnCancelListener  防止内存泄漏
 * <p>
 * Date: 2020-12-28
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
class WrapperSafeOnCancelListener(listener: DialogInterface.OnCancelListener?) : DialogInterface.OnCancelListener {

    private var mListener: WeakReference<DialogInterface.OnCancelListener> = WeakReference(listener)

    override fun onCancel(dialog: DialogInterface?) {
        mListener.get()?.onCancel(dialog)
    }
}