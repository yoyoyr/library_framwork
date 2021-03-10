package com.tde.framework.widget.recyclerview.footer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tde.framework.R
import com.tde.framework.widget.view.ProgressDrawable

/**
 * 默认加载中样式
 * <p>
 * Date: 2021-01-14
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
class DefaultLoadingFooter : IFooter {

    private var progressDrawable: ProgressDrawable? = null
    private var ivProgress: ImageView? = null

    override fun inflate(context: Context, viewGroup: ViewGroup): View {
        val view =
            LayoutInflater.from(context).inflate(R.layout.list_footer_loading, viewGroup, false)
        ivProgress = view.findViewById(R.id.footer_iv_progress)
        return view
    }

    override fun onAttach() {
        super.onAttach()
        if (progressDrawable == null) {
            progressDrawable = ProgressDrawable().apply { setColor(-0x99999a) }
            ivProgress?.setImageDrawable(progressDrawable)
        }
        progressDrawable?.start()
    }

    override fun onDetached() {
        super.onDetached()
        if (progressDrawable?.isRunning == true) {
            progressDrawable?.stop()
        }
    }
}