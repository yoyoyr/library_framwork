package com.tde.framework.binding

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tde.framework.binding.command.BindingCommand
import com.tde.framework.extensions.color
import com.tde.framework.extensions.idp
import com.tde.framework.statusBar.StatusBarUtil
import com.tde.framework.utils.LoggerUtils
import com.tde.framework.utils.ResourceUtils
import com.tde.framework.widget.view.OnMultiClickListener


/**
 * 通用的View bindingAdapter
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author:
 */
class ViewBindingAdapter

/**
 * requireAll 是意思是是否需要绑定全部参数, false为否
 * View的onClick事件绑定
 * onClickCommand 绑定的命令,
 * isThrottleFirst 是否开启防止过快点击
 */
@SuppressLint("CheckResult")
@BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
fun View.onClickCommand(clickCommand: BindingCommand<*>?, isThrottleFirst: Boolean? = true) {
    //不拦截
    if (isThrottleFirst == false) {
        setOnClickListener {
            clickCommand?.execute()
        }
    } else {
        //1秒内只能点击一次
        setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?) {
                clickCommand?.execute()
            }
        })
    }


}

@BindingAdapter(value = ["setColor"])
fun TextView.setColor(color: Int?) {
    color ?: return
    try {
        setTextColor(color.color())
    } catch (ex: Exception) {
        LoggerUtils.LOGW(ex)
    }
}

@BindingAdapter(value = ["srcObject"])
fun ImageView.setSrcObject(id: Any?) {

    try {
        Glide.with(context)
                .load(id)
                .into(this)
    } catch (ex: Exception) {
        LoggerUtils.LOGW(ex)
    }
}

/**
 * view的onLongClick事件绑定
 */
@SuppressLint("CheckResult")
@BindingAdapter(value = ["onLongClickCommand"], requireAll = false)
fun onLongClickCommand(view: View, clickCommand: BindingCommand<*>?) {
    view.setOnClickListener {
        clickCommand?.execute()
    }
}

/**
 * marginTop高度加上状态栏的高度   保证布局在statusBar下面
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver View
 * @param marginTop Int
 */
@BindingAdapter("margin_top_with_statusBar")
fun View.marginTopWithStatusBar(marginTop: Int) {
    this.setMargin(0, marginTop.idp() + StatusBarUtil.getStatusBarHeight(this.context), 0, 0)
}

@BindingAdapter("padding_top_with_statusBar")
fun View.paddingTopWithStatusBar(paddingTop: Int) {
    this.setPadding(0, paddingTop.idp() + StatusBarUtil.getStatusBarHeight(this.context), 0, 0)
}

@BindingAdapter("min_height_with_statusBar")
fun View.minimumHeight(minHeight: Int) {
    LoggerUtils.LOGV("minHeight = ${minHeight.idp()} , bar = ${StatusBarUtil.getStatusBarHeight(this.context)}")
    this.minimumHeight = minHeight.idp() + StatusBarUtil.getStatusBarHeight(this.context)
}


/**
 * 控件高度加上状态栏的高度   在某些沉浸式模式下，控件需要将高度加上状态栏的高度
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver View
 */
@BindingAdapter("height_with_statusBar")
fun View.heightWithStatusBar(height: Int) {
    val lp = layoutParams
    lp.height = height.idp() + StatusBarUtil.getStatusBarHeight(this.context)
    layoutParams = lp
}


/**
 * View设置margin
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver View
 */
@BindingAdapter(
        value = ["marginLeft", "marginTop", "marginRight", "marginBottom", "marginStart", "marginEnd"],
        requireAll = false
)
fun View.setMargin(
        marginLeft: Int?,
        marginTop: Int?,
        marginRight: Int?,
        marginBottom: Int?,
        marginStart: Int? = null,
        marginEnd: Int? = null
) {
    val lp = this.layoutParams
    if (lp is ViewGroup.MarginLayoutParams) {
        lp.leftMargin = marginLeft ?: lp.leftMargin
        lp.topMargin = marginTop ?: lp.topMargin
        lp.rightMargin = marginRight ?: lp.rightMargin
        lp.bottomMargin = marginBottom ?: lp.bottomMargin
        lp.marginStart = marginStart ?: lp.marginStart
        lp.marginEnd = marginEnd ?: lp.marginEnd
    }
    this.layoutParams = lp
}

/**
 * View设置padding  使用dataBinding进行设置
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver View
 */
@BindingAdapter(
        value = ["paddingLeft", "paddingTop", "paddingRight", "paddingBottom"],
        requireAll = false
)
fun View.setPadding(paddingLeft: Int?, paddingTop: Int?, paddingRight: Int?, paddingBottom: Int?) {
    this.setPadding(
            paddingLeft ?: this.paddingLeft,
            paddingTop ?: this.paddingTop,
            paddingRight ?: this.paddingRight,
            paddingBottom ?: this.paddingBottom
    )
}


/**
 * 设置纯色背景的颜色和圆角，避免使用
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver View
 * @param color Int 颜色之
 * @param roundRadius Float?   圆角大小
 * @param tl Float 左上圆角
 * @param tr Float 右上圆角
 * @param bl Float 左下
 * @param br Float 右下
 */
@BindingAdapter(
        "round_color",
        "round_radius",
        "tl_radius",
        "tr_radius",
        "bl_radius",
        "br_radius",
        requireAll = false
)
fun View.setRoundBg(
        color: Int,
        roundRadius: Float?,
        tl: Float = 0f,
        tr: Float = 0f,
        bl: Float = 0f,
        br: Float = 0f
) {
    if (roundRadius != null) {
        setBgDrawable(
                ResourceUtils.createRoundDrawable(
                        color,
                        roundRadius,
                        roundRadius,
                        roundRadius,
                        roundRadius
                )
        )
    } else {
        setBgDrawable(ResourceUtils.createRoundDrawable(color, tl, tr, bl, br))
    }
}

/**
 * 设置view的背景drawable
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-16
 * @receiver View
 * @param drawable Drawable
 */
fun View.setBgDrawable(drawable: Drawable) = ViewCompat.setBackground(this, drawable)