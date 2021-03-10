package com.tde.framework.web

import android.app.Application
import android.net.http.SslError
import android.webkit.*
import com.tde.framework.base.BaseViewModel
import com.tde.framework.base.model.SpaceModel
import com.tde.framework.binding.command.SingleLiveEvent

/**
 *  web view 容器类的viewModel
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * @param url String?
 * @property uiEvent UIChangeEvent
 * @property mWebViewClient WebViewClient
 * @constructor
 *
 * Author: yangrong
 */
class WebViewModel(application: Application, val url: String?) : BaseViewModel<SpaceModel>(application) {

    val uiEvent = UIChangeEvent()

    val mWebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            if (url.isNullOrBlank()) {
                //这边显示404或者错误页面
                return false
            }
            if (URLUtil.isNetworkUrl(url)) {
                view?.loadUrl(url)
            }
            return true
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)
            // 页面SSL证书存在问题, 不要使用handler.proceed()忽略该问题, 否则无法上架google商店，应该让H5同学确认修正
        }


        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            //异常错误
        }

        override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
            super.onReceivedHttpError(view, request, errorResponse)

        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            //加载结束
        }
    }


    //###################################通用基础JS事件处理#####################################

    /**
     * 关闭H5页面
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-27
     */
    @JavascriptInterface
    fun closeWeb() {
        finish()
    }

    /**
     * 重新加载
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-27
     */
    @JavascriptInterface
    fun reloadH5() {
        uiEvent.reloadH5Event.call()
    }

    /**
     * 有回退就回退，没回退就关闭
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-27
     */
    @JavascriptInterface
    fun goBack() {
        uiEvent.goBackEvent.call()
    }

    /**
     * 开启、关闭页面物理返回按键拦截
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-27
     * @param flag Int 1 开启拦截  0关闭拦截
     */
    @JavascriptInterface
    fun isInterceptBack(flag: Int) {
        uiEvent.interceptBackEvent.value = flag
    }

    class UIChangeEvent {
        /**
         * 重新加载H5页面
         */
        val reloadH5Event = SingleLiveEvent<Any>()

        /**
         * 有回退就回退，没回退就关闭
         */
        val goBackEvent = SingleLiveEvent<Any>()

        /**
         * 拦截物理返回按钮
         */
        val interceptBackEvent = SingleLiveEvent<Int>()
    }


}