package com.tde.framework.base

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.tde.framework.R
import com.tde.framework.dialog.loading.LoadingDialogProvider
import com.tde.framework.extensions.AndroidViewModelFactory
import com.tde.framework.statusBar.StatusBarUtil
import com.tde.framework.base.stack.ActivityStack
import com.tde.framework.base.stack.FragmentStack
import com.tde.framework.widget.title.TitleBarView
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<M : ViewDataBinding, VM : BaseViewModel<*>> :
    VisibleCallbackFragment() {

    /**
     * 计算用户浏览页面的时间
     */
    private var startTime = 0L

    //item_click埋点需要上送属性，在onClickTrackCommand()方法中获取并付值
    val pageCode = javaClass.simpleName

    private var pageLevel = 2 //activity + 自身就是 2
    private var prevPage: String? = ""
    private var page: String? = ""
    private var prePageCode: String? = ""

    lateinit var viewModel: VM

    private var rootView: View? = null

    abstract fun getLayoutId(): Int

    /**
     * 初始化ViewModel的id
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-11
     * @return Int
     */
    abstract fun initVariableId(): Int

    abstract fun initView()

    /**
     * 埋点对用的page字段
     * <p>
     * Author: yangrong
     * Date: 2021-02-07
     * @return String
     */
    abstract fun getPageName(): String

    lateinit var binding: M

    var titleBarView: TitleBarView? = null

    /**
     * 加载框
     */
    private var dialog: Dialog? = null

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
        super.onCreateView(inflater, container, savedInstanceState)
        if (rootView != null) {
            return rootView
        }
        if (showTitleBar()) {
            val view = LayoutInflater.from(requireContext())
                .inflate(R.layout.root_layout, container, false) as LinearLayout
            titleBarView = view.findViewById(R.id.titleBarView)
            binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            view.addView(binding.root)
            rootView = view
        } else {
            binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            rootView = binding.root
        }
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initViewDataBinding()
        //私有的ViewModel与View的契约事件回调逻辑
        registerUIChangeEventCallBack()
        initView()
        initViewObservable()

    }

    override fun onResume() {
        super.onResume()
        FragmentStack.addFragment(this)

    }

    fun getPageLevel(fragment: Fragment) {
        fragment.parentFragment?.run {
            pageLevel++
            getPageLevel(this)
        }

    }

    override fun onFragmentUnVisible() {
        FragmentStack.removeFragment(this)
    }

    override fun onFragmentVisible() {

    }


    override fun onDestroy() {
        super.onDestroy()
        if (::viewModel.isInitialized) {
            //解除ViewModel生命周期感应
            lifecycle.removeObserver(viewModel)
        }
        if (::binding.isInitialized) {
            binding.unbind()
        }

        dismissLoading()
    }


    fun isViewModelInit(): Boolean {
        return ::viewModel.isInitialized
    }


    open fun setStatusBarColor(activity: Activity, colorId: Int) {
        StatusBarUtil.setStatusBarColor(activity, colorId)
    }

    /**
     * binding绑定处理
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-24
     */
    @Suppress("UNCHECKED_CAST")
    private fun initViewDataBinding() {
        val viewModelId = initVariableId()
        binding.setVariable(viewModelId, viewModel)
        binding.lifecycleOwner = this
        lifecycle.addObserver(viewModel)
    }


    /**
     * 初始化viewModel
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-24
     */
    @Suppress("UNCHECKED_CAST")
    private fun initViewModel() {
        val viewModelType = getViewModelType()
        viewModel = createViewModel(this, viewModelType as Class<ViewModel>) as VM
    }

    open fun showTitleBar(): Boolean {
        return false
    }


    /**
     * 设置标题
     */
    open fun setTitle(name: String) {
        titleBarView?.setTitle(name)
    }

    fun setTitle(titleId: Int) {
        setTitle(getString(titleId))
    }

    /**
     * 隐藏返回按钮
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-18
     */
    fun hideBackBtn() {
        titleBarView?.hideBackBtn()
    }


    /**
     * 创建ViewModel  这个方法是私有的
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-011
     * @return T
     */
    protected open fun <T : ViewModel> createViewModel(fragment: Fragment, cls: Class<T>): T {
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

        viewModel.mUiEvent.finishEvent.observe(viewLifecycleOwner) { finish() }

    }

    /**
     * 默认不实现，有需要的业务自行去定义
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-29
     */
    open fun finish() {

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
        if (dialog == null) {
            dialog = LoadingDialogProvider.createLoadingDialog(requireActivity(), title)
        }
        dialog!!.setCancelable(isCancelable)
        dialog!!.setCanceledOnTouchOutside(isCancelOutside)
        dialog!!.setOnCancelListener(onCancelListener)
        dialog!!.show()
    }

    /**
     * 关闭加载对话框
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-24
     */
    open fun dismissLoading() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
            dialog = null
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (rootView as? ViewGroup)?.removeView(rootView)
    }

}