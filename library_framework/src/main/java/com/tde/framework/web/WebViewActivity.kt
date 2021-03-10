package com.tde.framework.web

import android.annotation.SuppressLint
import android.view.KeyEvent
import androidx.lifecycle.observe
import com.tde.framework.BR
import com.tde.framework.R
import com.tde.framework.base.BaseActivity
import com.tde.framework.databinding.BaseWebViewLayoutBinding

/**
 * 默认webView容器类，目前功能比较简单
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * @property canBack Boolean
 * @property title String
 * @property url String
 *
 * Author: yangrong
 */
@Suppress("DEPRECATION")
class WebViewActivity : BaseActivity<BaseWebViewLayoutBinding, WebViewModel>() {

    /**
     * 是否可以返回，如果被拦截了则设置为false
     */
    private var canBack = true

    /**
     * 网页标题
     */
    val title: String by lazy { intent.getStringExtra("title") ?: "" }

    /**
     * url
     */
    val url: String by lazy { intent.getStringExtra("url") ?: "" }

    override fun getLayoutId(): Int = R.layout.base_web_view_layout

    override fun initVariableId(): Int = BR.viewModel

    override fun createViewModel(): WebViewModel {
        return WebViewModel(application, url)
    }

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun initView() {
        setTitle(title)
        binding.mWebView.settings.apply {
            setSupportZoom(true)
            builtInZoomControls = true
            useWideViewPort = true
            loadWithOverviewMode = true
            javaScriptEnabled = true
            savePassword = false
            domStorageEnabled = true
            allowFileAccess = true
            defaultTextEncodingName = "utf-8"
        }
        binding.mWebView.addJavascriptInterface(viewModel, "jsObj")
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.uiEvent.reloadH5Event.observe(this) {
            binding.mWebView.reload()
        }

        viewModel.uiEvent.goBackEvent.observe(this) {
            if (binding.mWebView.canGoBack()) {
                binding.mWebView.goBack()
            }
        }

        viewModel.uiEvent.interceptBackEvent.observe(this) {
            canBack = it != 1
        }
    }


    override fun onPause() {
        super.onPause()
        binding.mWebView.onPause()
        binding.mWebView.pauseTimers()
    }

    override fun onResume() {
        super.onResume()
        binding.mWebView.resumeTimers()
        binding.mWebView.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.mWebView.destroy()
    }


    override fun onLeftClick() {
        super.onLeftClick()
        receiveWebBack()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            receiveBack()
            if (canBack) {
                //如果没有被拦截，则使用默认逻辑，判断是否可以回退
                if (binding.mWebView.canGoBack()) {
                    binding.mWebView.goBack()
                    return true
                }
            } else {
                //如果被拦截了，则什么逻辑都不用处理
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    /**
     * 拦截到物理按键返回事件
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-27
     */
    private fun receiveBack() {
        evaluateJavascript("receiveBack")
    }


    /**
     * 拦截到页面返回事件（原生导航栏）调用h5页面方法 receiveWebBack
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-27
     */
    private fun receiveWebBack() {
        evaluateJavascript("receiveWebBack")
    }


    fun evaluateJavascript(functionName: String, callback: ((String) -> Unit)? = null) {
        binding.mWebView.evaluateJavascript("javascript:${functionName}()") {
            callback?.invoke(it)
        }

    }


    companion object {

        /**
         * path
         */
        const val ROUTER_WEB_VIEW_PATH = "/web/WebViewActivity"

        /**
         * 标题入参的key
         */
        const val TITLE = "title"

        /**
         * url 参数key
         */
        const val URL = "url"

    }

    override fun getPageName(): String {
        return intent.getStringExtra("title") ?: ""
    }
}