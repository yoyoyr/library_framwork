package com.tde.framework.dialog

import android.content.DialogInterface
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.tde.framework.base.BaseDialogFragment
import com.tde.framework.base.BaseViewModel

/**
 * 顺序弹窗的基类
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * @param M : ViewDataBinding
 * @param VM : BaseViewModel<*>
 * @property dialogResultListener DialogResultListener?
 *
 * Author: yangrong
 */
abstract class BaseSequenceDialogFragment<M : ViewDataBinding, VM : BaseViewModel<*>> :
    BaseDialogFragment<M, VM>() {

    var dialogResultListener: DialogResultListener? = null

    open fun showForResult(
        supportManager: FragmentManager,
        tag: String,
        listener: DialogResultListener
    ) {
        if (!isNeedPop()) {
            listener.onDialogResult()
            return
        }
        dialogResultListener = listener
        show(supportManager, tag)
    }

    open fun isNeedPop() = true

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dialogResultListener?.onDialogResult()
    }

    interface DialogResultListener {
        fun onDialogResult()
    }

}