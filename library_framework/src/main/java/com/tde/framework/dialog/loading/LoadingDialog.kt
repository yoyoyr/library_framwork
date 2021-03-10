package com.tde.framework.dialog.loading

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.tde.framework.R

/**
 * 通用加载中Dialog
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
class LoadingDialog : Dialog {

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)


    class Builder(private val context: Context) {
        private var message: String? = null
        private var isShowMessage = true
        private var isCancelable = false
        private var isCancelOutside = false
        private var mOnCancelListener: DialogInterface.OnCancelListener? = null

        /**
         * 设置提示信息
         *
         * @param message
         * @return
         */
        fun setMessage(message: String?): Builder {
            this.message = message
            return this
        }

        /**
         * 设置是否显示提示信息
         *
         * @param isShowMessage
         * @return
         */
        fun setShowMessage(isShowMessage: Boolean): Builder {
            this.isShowMessage = isShowMessage
            return this
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */
        fun setCancelable(isCancelable: Boolean): Builder {
            this.isCancelable = isCancelable
            return this
        }

        /**
         * 设置是否可以取消
         *
         * @param isCancelOutside
         * @return
         */
        fun setCancelOutside(isCancelOutside: Boolean): Builder {
            this.isCancelOutside = isCancelOutside
            return this
        }

        /**
         * 设置取消监听
         *
         * @param  onCancelListener
         * @return
         */
        fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener?): Builder {
            mOnCancelListener = onCancelListener
            return this
        }

        fun create(): LoadingDialog {
            val inflater = LayoutInflater.from(context)
            val view: View = inflater.inflate(R.layout.base_dialog_loading_layout, null)
            val loadingDialog = LoadingDialog(context, R.style.BaseDialogStyle)
            val msgText = view.findViewById<TextView>(R.id.base_loading_tv_message)
            if (isShowMessage) {
                msgText.text = message
            } else {
                msgText.visibility = View.GONE
            }
            loadingDialog.setContentView(view)
            loadingDialog.setCancelable(isCancelable)
            loadingDialog.setCanceledOnTouchOutside(isCancelOutside)
            if (null != mOnCancelListener) {
                loadingDialog.setOnCancelListener(mOnCancelListener)
            }
            return loadingDialog
        }
    }

}