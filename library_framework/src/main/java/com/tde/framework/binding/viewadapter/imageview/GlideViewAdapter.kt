package com.tde.framework.binding.viewadapter.imageview

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

@BindingMethods(
        BindingMethod(type = ImageView::class, attribute = "imageUrl", method = "setImageUrl"),
        BindingMethod(type = ImageView::class, attribute = "colorPlaceholder", method = "setImageUrl"),
        BindingMethod(type = ImageView::class, attribute = "imagePlaceholder", method = "setImageUrl"),
        BindingMethod(type = ImageView::class, attribute = "resDrawable", method = "setImageUrl"),
        BindingMethod(type = ImageView::class, attribute = "priority", method = "setImageUrl"),
        BindingMethod(type = ImageView::class, attribute = "clearOnDetach", method = "setImageUrl"),
        BindingMethod(type = ImageView::class, attribute = "skipCache", method = "setImageUrl")
)
class GlideViewAdapter

/**
 * Desc: 配置Glide请求参数，根据需要选填
 * @param imageUrl             图片地址
 * @param colorPlaceholder     色值占位图
 * @param imagePlaceholder     图片占位图
 * @param resDrawable          res图片资源
 * @param priority             请求优先级
 * @param clearOnDetach        clearOnDetach
 * @param skipCache             不进行缓存
 */
@SuppressLint("CheckResult")
@BindingAdapter(
        value = ["imageUrl", "colorPlaceholder", "imagePlaceholder", "resDrawable", "priority", "clearOnDetach", "skipCache", "isCrossFade", "imageHeight", "imageWidth"],
        requireAll = false
)
fun ImageView.setImageUrl(
        imageUrl: String?,
        colorPlaceholder: Int,
        imagePlaceholder: Drawable?,
        resDrawable: Drawable?,
        priority: Priority?,
        clearOnDetach: Boolean?,
        skipCache: Boolean?,
        isCrossFade: Boolean = false,
        imageHeight: Int?,
        imageWidth: Int?
) {
    val context = context
    if (context is Activity && context.isDestroyed) {
        return
    }
    var placeholder: Drawable? = null
    if (colorPlaceholder != 0) {
        // 设置颜色占位图
        val drawable = GradientDrawable()
        drawable.setColor(Color.parseColor(String.format("#%x", colorPlaceholder)))
        placeholder = drawable
    } else if (imagePlaceholder != null) {
        // 设置图片占位图
        placeholder = imagePlaceholder
    }
    // 资源为null，加载占位图
    if (resDrawable == null && imageUrl.isNullOrBlank()) {
        Glide.with(this).clear(this)
        this.setImageDrawable(placeholder)
        return
    }

    var builder: RequestBuilder<Drawable?>
    // 设置image url、image Key
    builder = when {
        resDrawable != null -> { // 加载res图片
            Glide.with(this).load(resDrawable)
        }
        else -> { // 加载网络图片，使用imageKey
            Glide.with(this).load(imageUrl)
        }
    }

    builder = builder.apply(RequestOptions.placeholderOf(placeholder))

    if (imageHeight != null && imageWidth != null) {
        builder.override(imageWidth, imageHeight)
    }


    if (skipCache == true) {
        builder = builder.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
    }
    // 设置图片圆角、圆形
    var requestOptions = RequestOptions()
    if (isCrossFade) { //默认300ms
        builder = builder.transition(
                DrawableTransitionOptions.withCrossFade(
                        DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true)
                )
        )
    }
    requestOptions = if (priority != null) {
        requestOptions.priority(priority)
    } else {
        requestOptions.priority(Priority.NORMAL)
    }
    val viewTarget = builder.apply(requestOptions).into(this)
    if (clearOnDetach == true) {
        viewTarget.clearOnDetach()
    }
}

/**
 * imageView 设置drawable  内部使用Glide实现
 * <p>
 * Author: zhuanghongzhan
 * Date: 2021-02-09
 * @receiver ImageView
 */
fun ImageView.setDrawable(drawable: Drawable) {
    Glide.with(this).load(drawable).into(this)
}
