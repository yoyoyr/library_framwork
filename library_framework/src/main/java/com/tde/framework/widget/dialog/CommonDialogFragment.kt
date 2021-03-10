package com.tde.framework.widget.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tde.framework.R
import com.tde.framework.base.VisibleCallbackDialogFragment
import com.tde.framework.widget.dialog.leak.LeakSafeDialog


/**
 * 通用DialogFragment
 * <p>
 * Date: 2020-12-28
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
abstract class CommonDialogFragment : VisibleCallbackDialogFragment() {

    private var mOnDismissListener: DialogInterface.OnDismissListener? = null

    private var mOnCancelListener: DialogInterface.OnCancelListener? = null


    fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        this.mOnDismissListener = listener
    }

    fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        this.mOnCancelListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.BaseCustomDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return LeakSafeDialog(requireActivity(), theme)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mOnDismissListener?.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        mOnCancelListener?.onCancel(dialog)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        this.show(manager.beginTransaction(), tag)
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        if (isResumed || isAdded || isVisible || isRemoving || isDetached) {
            return -1
        }
        return try {
            transaction.add(this, tag).commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }


    override fun dismiss() {
        try {
            super.dismissAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}