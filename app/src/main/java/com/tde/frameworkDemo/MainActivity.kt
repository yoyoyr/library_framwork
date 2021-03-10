package com.tde.frameworkDemo

import android.view.View
import androidx.core.content.ContextCompat
import com.ned.frameworkDemo.R
import com.ned.frameworkDemo.BR
import com.ned.frameworkDemo.databinding.MainBinding
import com.tde.framework.base.BaseActivity
import com.tde.framework.utils.LoggerUtils

class MainActivity : BaseActivity<MainBinding, MainViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.main
    }

    override fun initView() {
        setStatusBarColor(this, ContextCompat.getColor(this, R.color.color_00ffffff))
        setTitle("首页")
        setRightText("分享")

        LoggerUtils.LOGV("main init")
//        setRightClickListener(View.OnClickListener { ToastUtils.showShort("分享成功") })
    }

    override fun fitsSystemWindows(): Boolean {
        return true
    }
    
    fun goSet(view: View) {
        LoggerUtils.LOGV("goSet")
//        ARouter.getInstance().build("/app/SetActivity").navigation()
    }

    override fun initVariableId() = BR.viewModel
    override fun getPageName(): String {
        return ""
    }

}