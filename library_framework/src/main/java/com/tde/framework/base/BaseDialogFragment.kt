package com.tde.framework.base

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.tde.framework.dialog.loading.LoadingDialogProvider
import com.tde.framework.extensions.AndroidViewModelFactory
import com.tde.framework.base.stack.ActivityStack
import com.tde.framework.base.stack.FragmentStack
import com.tde.framework.widget.dialog.CommonDialogFragment
import java.lang.reflect.ParameterizedType

/**
 * 基础DialogFragment
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * @param M : ViewDataBinding
 * @param VM : BaseViewModel<*>
 * @property viewModel VM
 * @property binding M
 * @property loadingDialog Dialog?
 * @property startTime Long
 * @property pageCode (kotlin.String..kotlin.String?)
 * @property pageLevel Int
 * @property prevPage String?
 * @property prePageCode String?
 *
 * Author:
 */
abstract class BaseDialogFragment<M : ViewDataBinding, VM : BaseViewModel<*>> :
    CommonDialogFragment() {

    protected lateinit var viewModel: VM

    protected lateinit var binding: M

    /**
     * 加载框
     */
    private var loadingDialog: Dialog? = null

    private var startTime = 0L
    //item_click埋点需要上送属性，在onClickTrackCommand()方法中获取并付值
    val pageCode = javaClass.simpleName
    private var pageLevel = 2
    private var prevPage: String? = ""
    private var prePageCode: String? = ""

    /**
     * 埋点对用的page字段
     * <p>
     * Author: yangrong
     * Date: 2021-02-07
     * @return String
     */
    abstract fun getPageName():String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams()
    }


    override fun onFragmentUnVisible() {
        FragmentStack.removeFragment(this)
    }

    override fun onFragmentVisible() {
        FragmentStack.addFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            prePageCode = ActivityStack.getActivityStack().lastElement().javaClass.simpleName
            prevPage = (ActivityStack.getActivityStack()
                .lastElement() as BaseActivity<*, *>).getPageName()
        } catch (ex: Exception) { //第一个页面
            prevPage = ""
        }
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initVariableId(): Int


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initViewDataBinding()
        registerUIChangeEventCallBack()
        initView()
        initViewObservable()

    }

    @Suppress("UNCHECKED_CAST")
    private fun initViewModel() {
        val viewModelType = getViewModelType()
        viewModel = createViewModel(this, viewModelType as Class<BaseViewModel<*>>) as VM
    }

    /**
     * 创建ViewModel  这个方法是私有的
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-011
     * @return T
     */
    protected open fun <T : BaseViewModel<*>> createViewModel(
        fragment: Fragment,
        cls: Class<T>
    ): T {
        return ViewModelProvider(
            fragment,
            AndroidViewModelFactory(requireActivity().application, ::createViewModel)
        )[cls]
    }

    /**
     * 创建ViewModel  如果有viewModel的构造有多个参数则使用该方法
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-11
     * @return ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    protected open fun createViewModel(): VM {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(getViewModelType() as Class<VM>)
    }

    /**
     * binding绑定处理
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-24
     */
    private fun initViewDataBinding() {
        val viewModelId = initVariableId()
        binding.setVariable(viewModelId, viewModel)
        binding.lifecycleOwner = this
        lifecycle.addObserver(viewModel)
    }


    /**
     * 获取viewModel类型
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-24
     * @return Class<*>
     */
    private fun getViewModelType(): Class<*> {
        val modelClass: Class<*>
        val type = javaClass.genericSuperclass
        modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[1] as Class<*>
        } else {
            //如果没有指定泛型参数，则默认使用BaseViewModel
            BaseViewModel::class.java
        }
        return modelClass
    }


    /**
     * 处理viewModel事件
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-23
     */
    private fun registerUIChangeEventCallBack() {
        viewModel.mUiEvent.startActivityEvent.observe(viewLifecycleOwner) { params ->
            val clz = params[BaseViewModel.ParameterField.CLASS] as Class<*>
            val bundle = params[BaseViewModel.ParameterField.BUNDLE] as? Bundle
            startActivity(clz, bundle)
        }

        viewModel.mUiEvent.showDialogEvent.observe(viewLifecycleOwner) {
            showLoading(it.title, it.isCancelable, it.isCancelOutside, it.onCancelListener)
        }

        viewModel.mUiEvent.dismissDialogEvent.observe(viewLifecycleOwner) { dismissLoading() }

        viewModel.mUiEvent.finishEvent.observe(viewLifecycleOwner) { dismissAllowingStateLoss() }
    }


    /**
     * 显示加载对话框
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-24
     * @param title String? 加载标题
     * @param isCancelable Boolean 是否可以关闭
     * @param isCancelOutside Boolean 是否可以点击外部管不
     * @param onCancelListener OnCancelListener? dialog关闭监听
     */
    open fun showLoading(
        title: String? = null,
        isCancelable: Boolean,
        isCancelOutside: Boolean,
        onCancelListener: DialogInterface.OnCancelListener?
    ) {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialogProvider.createLoadingDialog(requireActivity(), title)
        }
        loadingDialog!!.setCancelable(isCancelable)
        loadingDialog!!.setCanceledOnTouchOutside(isCancelOutside)
        loadingDialog!!.setOnCancelListener(onCancelListener)
        loadingDialog!!.show()
    }

    /**
     * 关闭加载对话框
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-24
     */
    open fun dismissLoading() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
            loadingDialog = null
        }
    }

    /**
     * 跳转页面
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-23
     * @param clz Class<*>? 需要跳转的activity类
     * @param bundle Bundle? 跳转时的传参
     */
    fun startActivity(clz: Class<*>?, bundle: Bundle? = null) {
        val intent = Intent(context, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }


    /**
     * 处理ui通知，如liveDate
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-24
     */
    open fun initViewObservable() {
        //...
    }

    open fun initView() {

    }

    open fun initParams() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (::viewModel.isInitialized) {
            //解除ViewModel生命周期感应
            lifecycle.removeObserver(viewModel)
        }
        if (::binding.isInitialized) {
            binding.unbind()
        }
    }

}