package com.tde.framework.binding.viewadapter.recyclerview

import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapters


@BindingMethods(
        BindingMethod(type = BindingRecyclerViewAdapters::class, attribute = "itemBinding", method = "setAdapter"),
        BindingMethod(type = BindingRecyclerViewAdapters::class, attribute = "items", method = "setAdapter")
)
class ViewAdapter

/**
 * 设置itemDecoration
 * <p>
 * Author: zhuanghongzhan
 * Date: 2021-01-06
 * @receiver RecyclerView
 * @param itemDecoration ItemDecoration?
 */
@BindingAdapter("itemDecoration")
fun RecyclerView.setItemDecoration(itemDecoration: RecyclerView.ItemDecoration?) {
    itemDecoration ?: return
    //移除后在加入
    removeItemDecoration(itemDecoration)
    addItemDecoration(itemDecoration)
}

@BindingAdapter("hasFixedSize")
fun RecyclerView.setFixedSize(hasFixedSize: Boolean) {
    setHasFixedSize(hasFixedSize)
}


/**
 * recycler 设置OnItemTouchListener binding属性
 * <p>
 * Author: zhuanghongzhan
 * Date: 2021-01-07
 * @receiver RecyclerView
 * @param listener OnItemTouchListener?
 */
@BindingAdapter("onItemTouchListener")
fun RecyclerView.setOnItemTouchListener(listener: RecyclerView.OnItemTouchListener?) {
    listener ?: return
    //移除后在加入
    removeOnItemTouchListener(listener)
    addOnItemTouchListener(listener)
}

/**
 * 设置item动画
 * <p>
 * Author: zhuanghongzhan
 * Date: 2021-01-08
 * @receiver RecyclerView
 * @param animator ItemAnimator?
 */
@BindingAdapter("itemAnimator")
fun RecyclerView.setBindingItemAnimator(animator: RecyclerView.ItemAnimator?) {
    itemAnimator = animator
}

/**
 * 关闭动画
 */
@BindingAdapter("closeAnim")
fun RecyclerView.closeAnim(close:Boolean){
    if(close){
        itemAnimator?.addDuration = 0
        itemAnimator?.changeDuration = 0
        itemAnimator?.moveDuration = 0
        itemAnimator?.removeDuration = 0
        (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
    }

}
