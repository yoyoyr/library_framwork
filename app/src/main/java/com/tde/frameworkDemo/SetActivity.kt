package com.tde.frameworkDemo

import android.view.View
import androidx.core.content.ContextCompat
import com.ned.frameworkDemo.R
import com.ned.frameworkDemo.BR
import com.ned.frameworkDemo.databinding.SetBinding
import com.tde.framework.base.BaseActivity


class SetActivity : BaseActivity<SetBinding, SetViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.set
    }

    override fun initView() {
        setStatusBarColor(this, ContextCompat.getColor(this, R.color.color_00ffffff))
        setTitle("设置")
        setRightText("分享")
//        setRightClickListener(View.OnClickListener { ToastView.show(this,"分享成功") })
    }

    override fun fitsSystemWindows(): Boolean {
        return true
    }

    fun goMain(view: View) {
//        ARouter.getInstance().build("/app/MainActivity").navigation()
    }

    override fun initVariableId(): Int = BR.viewModel

    /**
     * 如果需要传入参数构造则使用该方法
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-04
     * @return ViewModel
     */
    override fun createViewModel(): SetViewModel {
        return SetViewModel(application,"测试第二个参数")

    }

    override fun getPageName(): String {
        return ""
    }

}