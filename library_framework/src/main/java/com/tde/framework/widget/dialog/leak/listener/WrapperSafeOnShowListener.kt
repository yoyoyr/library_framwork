package com.tde.framework.widget.dialog.leak.listener

import android.content.DialogInterface
import java.lang.ref.WeakReference

/**
 * 弱引用的OnShowListener  防止内存泄漏
 * <p>
 * Date: 2020-12-28
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
class WrapperSafeOnShowListener(listener: DialogInterface.OnShowListener?) : DialogInterface.OnShowListener {

    private var mListener: WeakReference<DialogInterface.OnShowListener> = WeakReference(listener)
    override fun onShow(dialog: DialogInterface?) {
        mListener.get()?.onShow(dialog)
    }

}