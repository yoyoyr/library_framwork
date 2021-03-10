package com.tde.framework.widget.recyclerview.footer

import android.content.Context
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tde.framework.binding.command.BindingCommand

/**
 * 上拉加载的Footer容器
 * <p>
 * Date: 2021-01-14
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
class LoadFooter : FrameLayout {

    /**
     * 无更多数据
     */
    private val noMoreFooter by lazy(LazyThreadSafetyMode.NONE) {
        FooterUtils.getFooterProvider().createNoMoreFooter()
    }

    /**
     * 加载中
     */
    private val loadingFooter by lazy(LazyThreadSafetyMode.NONE) {
        FooterUtils.getFooterProvider().createLoadingFooter()
    }

    /**
     * 加载失败
     */
    private val loadFailedFooter by lazy(LazyThreadSafetyMode.NONE) {
        FooterUtils.getFooterProvider().createLoadFailedFooter()
    }

    /**
     * 当前显示的Fotoer
     */
    private var currentFooter: IFooter? = null

    /**
     * 保存footer的view，避免每次都inflate
     */
    private val views = ArrayMap<IFooter, View>()

    /**
     * 点击重试
     */
    internal var retryCall: (() -> Unit)? = null
    private var retryClickCommand: BindingCommand<Any>? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * 设置footer状态
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @param status Int
     */
    fun setFooterStatus(@FooterStatus status: Int) {
        when (status) {
            FooterStatus.STATUS_NONE -> {
                if (currentFooter != null) {
                    removeFooter(currentFooter!!)
                    currentFooter = null
                }
            }
            FooterStatus.STATUS_NO_MORE -> {
                addFooter(noMoreFooter)
            }
            FooterStatus.STATUS_FAILED -> {
                addFooter(loadFailedFooter)
                views[loadFailedFooter]?.setOnClickListener {
                    retryCall?.invoke()
                    retryClickCommand?.execute()
                }
            }
            FooterStatus.STATUS_LOADING -> {
                addFooter(loadingFooter)
            }
        }
    }

    /**
     * 点击重试
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @param retryClickCommand BindingCommand<Any>
     */
    fun setRetryClickCommand(retryClickCommand: BindingCommand<Any>) {
        this.retryClickCommand = retryClickCommand
    }

    /**
     * 添加footer
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @param footer IFooter
     */
    private fun addFooter(footer: IFooter) {
        if (currentFooter == footer) {
            return
        }
        if (currentFooter != null) {
            removeFooter(currentFooter!!)
        }
        var view = views[footer]
        if (view == null) {
            view = footer.inflate(context, this)
            views[footer] = view
        }
        addView(view)
        footer.onAttach()
        currentFooter = footer
    }

    /**
     * 移除当前footer
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @param footer IFooter
     */
    private fun removeFooter(footer: IFooter) {
        val view = views[footer]
        if (view != null) {
            removeView(view)
            footer.onDetached()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        currentFooter?.onDetached()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        currentFooter?.onAttach()
    }
}