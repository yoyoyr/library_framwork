package com.tde.framework.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.tde.framework.R
import com.tde.framework.dialog.loading.LoadingDialogProvider
import com.tde.framework.extensions.AndroidViewModelFactory
import com.tde.framework.statusBar.StatusBarUtil
import com.tde.framework.swipeback.SwipeBackActivityHelper
import com.tde.framework.swipeback.SwipeBackLayout
import com.tde.framework.base.stack.ActivityStack
import com.tde.framework.widget.title.GlobalTitleBarProvider
import com.tde.framework.widget.title.TitleBarView
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.title_bar.view.*
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<T : ViewDataBinding, VM : BaseViewModel<*>> :
    AppCompatActivity() {


    /**
     * 计算用户浏览页面的时间
     */
    private var startTime = 0L

    //item_click埋点需要上送属性，在onClickTrackCommand()方法中获取并付值
    public val pageCode = javaClass.simpleName
    private var pageLevel = 1//activity的层级一直是1
    private var prevPage: String? = ""
    private var prePageCode: String? = ""
    private var windowFocus = false

    lateinit var binding: T

    protected lateinit var viewModel: VM

    private var viewModelId: Int = 0

    abstract fun getLayoutId(): Int

    /**
     * 埋点对用的page字段app
     * <p>
     * Author: yangrong
     * Date: 2021-02-07
     * @return String
     */
    abstract fun getPageName(): String

    /**
     * 初始化ViewModel的id
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-04
     * @return Int
     */
    abstract fun initVariableId(): Int

    abstract fun initView()

    var titleBarView: TitleBarView? = null

    /**
     * 加载框
     */
    private var dialog: Dialog? = null

    private val titleBarBuilder = GlobalTitleBarProvider.getTitleBarBuilder()


    private var mHelper: SwipeBackActivityHelper? = null
    private var mSwipeBackLayout: SwipeBackLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
//        if (forcePortrait()) {
//            try {
//                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//            } catch (e: Exception) {
//                //..
//            }
//        }
        try {
            prePageCode = ActivityStack.getActivityStack().lastElement().javaClass.simpleName
            prevPage = (ActivityStack.getActivityStack()
                .lastElement() as BaseActivity<*, *>).getPageName()
        } catch (ex: Exception) { //第一个页面
            prevPage = ""
        }
        super.onCreate(savedInstanceState)
        initParams()
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        addTitleBar(showTitleBar())
        initViewModel()
        binding.setVariable(viewModelId, viewModel)
        binding.lifecycleOwner = this
        if (fitsSystemWindows()) {
            StatusBarUtil.setStatusBarColor(this, titleBarBuilder.getStatusBarColor())
        } else {
            StatusBarUtil.setStatusBarColor(this, Color.TRANSPARENT)
            StatusBarUtil.setFitsSystemWindows(this, fitsSystemWindows())
        }

        initSwipeBackLayout()
        registerUIChangeEventCallBack()
        initView()
        initViewObservable()
    }


    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
        binding.unbind()
    }

    open fun initParams() {

    }

    /**
     * 是否强制竖屏
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-29
     */
    open fun forcePortrait(): Boolean {
        return true
    }


    /**
     * 添加通用标题栏
     */
    @SuppressLint("InflateParams")
    private fun addTitleBar(showTitleBar: Boolean) {
        if (showTitleBar) {
            val contentView = findViewById<ViewGroup>(android.R.id.content)
            if (contentView.childCount > 0) {
                val view =
                    LayoutInflater.from(this).inflate(
                        R.layout.root_layout,
                        null
                    ) as LinearLayout
                titleBarView = view.findViewById(R.id.titleBarView)
                val contain = contentView[0]
                contentView.removeView(contain)
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                view.addView(contain, params)
                val containParams = contain.layoutParams
                contentView.addView(view, containParams)
                initToolBar()
            }
        }
    }

    private fun initToolBar() {
        titleBarView?.setTitleBarBuilder(titleBarBuilder)
        titleBarView?.setLeftClickListener { onLeftClick() }

        titleBarView?.tvTitle?.setOnClickListener {
            //如果设置点击标题可返回，则生效
            if (titleBarBuilder.clickTitleToBack) {
                onLeftClick()
            }
        }

    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (mHelper != null) {
            mHelper!!.onPostCreate()
        }
    }

    /**
     * 关闭向右滑
     */
    open fun closeSwipeBackLayout() {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout!!.setEnableGesture(false)
        }
    }

    open fun openSwipeBackLayout() {
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout!!.setEnableGesture(true)
        }
    }

    /**
     * 是沉浸式  true:不沉浸式 false：沉浸式
     */
    open fun fitsSystemWindows(): Boolean {
        return true
    }

    open fun showTitleBar(): Boolean {
        return true
    }

    /**
     * 设置状态栏颜色
     */
    open fun setStatusBarColor(activity: Activity, colorId: Int) {
        StatusBarUtil.setStatusBarColor(activity, colorId)
    }

    /**
     * 初始化左滑关闭
     */
    open fun initSwipeBackLayout() {
        mHelper = SwipeBackActivityHelper(this)
        mHelper?.onActivityCreate()
        mSwipeBackLayout = mHelper?.swipeBackLayout
        mSwipeBackLayout?.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)
    }

    /**
     * 设置标题
     */
    open fun setTitle(name: String?) {
        titleBarView?.run {
            name?.let {
                setTitle(it)
            }
        }
    }

    fun setTitle(titleId: Int, rightImage: Int, onClick: () -> Unit = {}) {
        titleBarView?.run {
            setTitle(getString(titleId))
            setRightImage(getDrawable(rightImage))
            tvRight?.setOnClickListener {
                onClick.invoke()
            }
        }
    }

    fun setTitle(title: String?, rightImage: Int, onClick: () -> Unit = {}) {
        titleBarView?.run {
            setTitle(title)
            setRightImage(getDrawable(rightImage))
            tvRight?.setOnClickListener {
                onClick.invoke()
            }
        }
    }

    override fun setTitle(titleId: Int) {
        titleBarView?.setTitle(getString(titleId))
    }

    fun dismissTitle() {
        titleBarView?.visibility = View.GONE
    }


    /**
     * 设置标题栏右边名称
     */
    open fun setRightText(text: String) {
        titleBarView?.setRightText(text)
    }

    /**
     * 设置右边点击事件
     */
    open fun setRightClickListener(clickListener: View.OnClickListener) {
        titleBarView?.setRightClickListener(clickListener)
    }

    /**
     * 左边按钮点击事件，默认是关闭页面
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-18
     */
    open fun onLeftClick() {
        onBackPressed()
    }


    @Suppress("UNUSED_PARAMETER")
    fun closeActivity(view: View) {
        finish()
    }


    @Suppress("UNCHECKED_CAST")
    private fun initViewModel() {
        viewModelId = initVariableId()
        val viewModelType = getViewModelType()
        viewModel = createViewModel(this, viewModelType as Class<ViewModel>) as VM
        //关联ViewModel
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel)

    }


    /**
     * 创建ViewModel  这个方法是私有的
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-04
     * @param activity FragmentActivity
     * @param cls Class<T>
     * @return T
     */
    protected open fun <T : ViewModel> createViewModel(
        activity: FragmentActivity,
        cls: Class<T>
    ): T {
        return ViewModelProvider(
            activity,
            AndroidViewModelFactory(application, ::createViewModel)
        )[cls]
    }

    /**
     * 创建ViewModel  如果有viewModel的构造有多个参数则使用该方法
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-04
     * @return ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    protected open fun createViewModel(): VM {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(getViewModelType() as Class<VM>)
    }


    /**
     * 获取viewModel类型
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-24
     * @return Class<*>
     */
    open fun getViewModelType(): Class<*> {
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
        viewModel.mUiEvent.startActivityEvent.observe(this) { params ->
            val clz = params[BaseViewModel.ParameterField.CLASS] as Class<*>
            val bundle = params[BaseViewModel.ParameterField.BUNDLE] as? Bundle
            startActivity(clz, bundle)
        }

        viewModel.mUiEvent.showDialogEvent.observe(this, Observer {
            showLoading(it.title, it.isCancelable, it.isCancelOutside, it.onCancelListener)
        })

        viewModel.mUiEvent.dismissDialogEvent.observe(this) { dismissLoading() }
        viewModel.mUiEvent.finishEvent.observe(this) { finish() }

        viewModel.mUiEvent.onBackPressedEvent.observe(this) { onBackPressed() }
    }

    /**
     * 处理ui通知，如liveDate
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-24
     */
    open fun initViewObservable() {

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
            dialog = LoadingDialogProvider.createLoadingDialog(this, title)
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
     * @param bundle Bundle? 跳转时的传参a
     */
    fun startActivity(clz: Class<*>?, bundle: Bundle? = null) {
        val intent = Intent(this, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

}