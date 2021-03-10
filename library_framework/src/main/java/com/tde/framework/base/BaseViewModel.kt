package com.tde.framework.base

import android.app.Application
import android.app.SearchManager
import android.content.DialogInterface
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import com.tde.framework.base.BaseViewModel.ParameterField.CLASS
import com.tde.framework.base.model.BaseModel
import com.tde.framework.binding.command.BindingAction
import com.tde.framework.binding.command.BindingCommand
import com.tde.framework.binding.command.SingleLiveEvent
import com.tde.framework.dialog.loading.ShowDialogEntity
import com.tde.framework.lifecycle.IBaseLifecycle
import com.tde.framework.utils.ReflectionUtil


open class BaseViewModel<out M : BaseModel>(application: Application) :
    AndroidViewModel(application), IBaseLifecycle {

    /**
     * 数据仓库model
     */
    @Suppress("UNCHECKED_CAST")
    val model: M by lazy { ReflectionUtil.getNewInstance<M>(this, 0) }

    val mUiEvent by lazy { UIChangeEvent() }

    /**
     * 左边按钮点击事件
     */
    val leftIconClick = BindingCommand<Any>(BindingAction {
        onLeftIconClick()
    })


    /**
     * 跳转activity
     * 如果没有跨模块的话可以直接使用startActivity进行跳转
     * 如果跨模块则应该使用路由进行跳转
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-23
     * @param clz Class<*>
     * @param bundle Bundle?
     */
    fun startActivity(clz: Class<*>, bundle: Bundle? = null) {
        val params = HashMap<String, Any>()
        params[CLASS] = clz
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        mUiEvent.startActivityEvent.postValue(params)
    }


    fun showLoading(title: String? = null) {
        showLoading(title, isCancelable = true, isCancelOutside = false,
            onCancelListener = DialogInterface.OnCancelListener { finish() })
    }

    fun showLoading(
        title: String? = null,
        isCancelable: Boolean,
        isCancelOutside: Boolean,
        onCancelListener: DialogInterface.OnCancelListener?
    ) {
        mUiEvent.showDialogEvent.postValue(
            ShowDialogEntity(
                title,
                isCancelable,
                isCancelOutside,
                onCancelListener
            )
        )
    }

    fun dismissLoading() {
        mUiEvent.dismissDialogEvent.postValue(null)
    }

    open fun onLeftIconClick() {
        onBackPressed()
    }

    /**
     * 关闭界面
     */
    open fun finish() {
        mUiEvent.finishEvent.call()
    }

    /**
     * 返回
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-15
     */
    open fun onBackPressed() {
        mUiEvent.onBackPressedEvent.call()
    }


    /**
     * viewModel通知activity的event
     * <p>
     * Date: 2020-12-23
     * Company:
     * Author: zhuanghongzhan
     */
    class UIChangeEvent {
        //跳转activity
        val startActivityEvent by lazy(LazyThreadSafetyMode.NONE) { SingleLiveEvent<Map<String, Any>>() }

        //显示加载对话框
        val showDialogEvent by lazy(LazyThreadSafetyMode.NONE) { SingleLiveEvent<ShowDialogEntity>() }

        //关闭加载对话框
        val dismissDialogEvent by lazy(LazyThreadSafetyMode.NONE) { SingleLiveEvent<Void>() }

        //关闭页面
        val finishEvent by lazy(LazyThreadSafetyMode.NONE) { SingleLiveEvent<Void>() }

        //返回事件
        val onBackPressedEvent by lazy(LazyThreadSafetyMode.NONE) { SingleLiveEvent<Any>() }


    }

    object ParameterField {
        const val CLASS = "CLASS"
        const val BUNDLE = "BUNDLE"
    }

}