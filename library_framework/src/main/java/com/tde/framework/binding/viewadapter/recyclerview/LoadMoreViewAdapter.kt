package com.tde.framework.binding.viewadapter.recyclerview

import androidx.databinding.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tde.framework.R
import com.tde.framework.binding.collections.DiffObservableArrayList
import com.tde.framework.binding.command.BindingCommand
import com.tde.framework.widget.recyclerview.LoadMoreBindingRecyclerViewAdapter
import com.tde.framework.widget.recyclerview.footer.FooterStatus
import com.tde.framework.widget.recyclerview.preload.OnLoadMoreListener
import com.tde.framework.widget.recyclerview.preload.PreLoadScrollListener
import com.tde.framework.widget.recyclerview.preload.PullFromBottomHelper
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * RecyclerView分页加载
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author:
 */
@BindingMethods(
        BindingMethod(type = RecyclerView::class, attribute = "itemBinding", method = "setAdapter"),
        BindingMethod(type = RecyclerView::class, attribute = "items", method = "setAdapter"),
        BindingMethod(type = RecyclerView::class, attribute = "adapter", method = "setAdapter"),
        BindingMethod(type = RecyclerView::class, attribute = "itemIds", method = "setAdapter"),
        BindingMethod(type = RecyclerView::class, attribute = "loadStatus", method = "setAdapter"),
        BindingMethod(type = RecyclerView::class, attribute = "viewHolder", method = "setAdapter"),
        BindingMethod(type = RecyclerView::class, attribute = "onLoadMoreCommand", method = "setAdapter"),
        BindingMethod(type = RecyclerView::class, attribute = "loadStatusAttrChanged", method = "setAdapter"),
        BindingMethod(type = RecyclerView::class, attribute = "footerMarginBottom", method = "setAdapter")
)
class RecyclerViewLoadMore

@Suppress("UNCHECKED_CAST")
@BindingAdapter(value = ["itemBinding", "items", "adapter", "itemIds", "loadStatus", "viewHolder", "onLoadMoreCommand", "loadStatusAttrChanged", "footerMarginBottom"], requireAll = false)
fun <T> RecyclerView.setAdapter(itemBinding: ItemBinding<*>?,
                                items: List<*>?,
                                adapter: LoadMoreBindingRecyclerViewAdapter<T>?,
                                itemIds: BindingRecyclerViewAdapter.ItemIds<in T>?,
                                loadStatus: Int?,
                                viewHolderFactory: BindingRecyclerViewAdapter.ViewHolderFactory?,
                                onLoadMoreCommand: BindingCommand<Any>?,
                                bindingListener: InverseBindingListener?,
                                footerMarginBottom: Int) {
    var newAdapter = adapter
    requireNotNull(itemBinding) { "itemBinding must not be null" }
    val oldAdapter = this.adapter as? LoadMoreBindingRecyclerViewAdapter<T>
    if (newAdapter == null) {
        newAdapter = oldAdapter ?: LoadMoreBindingRecyclerViewAdapter()
    }
    if (onLoadMoreCommand != null) {
        var preLoadScrollListener = this.getTag(R.id.PreLoadScrollListenerId) as? PreLoadScrollListener
        if (preLoadScrollListener == null) {
            val onLoadMoreListener = object : OnLoadMoreListener {
                override fun onLoadMore() {
                    bindingListener?.onChange()
                    onLoadMoreCommand.execute()
                }
            }
            preLoadScrollListener = PreLoadScrollListener(onLoadMoreListener, 2)
            newAdapter.retryCall = { onLoadMoreListener.onLoadMore() }
            this.addOnScrollListener(preLoadScrollListener)
            this.setTag(R.id.PreLoadScrollListenerId, preLoadScrollListener)
            PullFromBottomHelper(this, onLoadMoreListener, false)
        }
    }
    newAdapter.itemBinding = itemBinding as ItemBinding<T>
    newAdapter.setItems(items as? List<T>)
    newAdapter.setItemIds(itemIds)
    newAdapter.setViewHolderFactory(viewHolderFactory)
    newAdapter.setFooterMarginBottom(footerMarginBottom)
    if (loadStatus != null) {
        newAdapter.setFooterStatus(loadStatus)
    }
    if (oldAdapter !== newAdapter) {
        this.adapter = newAdapter
        registerScrollToTop(this, items)
    }
}

/**
 * 如果往列表前面插入数据，且当前列表位于顶部且当前未处于滑动状态，则自动滑动到顶部
 * 解决往列表前面插入数据，未定位到顶部的问题
 * <p>
 * Author: zhuanghongzhan
 * Date: 2021-01-14
 * @param recyclerView RecyclerView
 * @param items List<T>?
 */
private fun <T> registerScrollToTop(recyclerView: RecyclerView, items: List<T>?) {
    if (items is DiffObservableArrayList) {
        items.onDispatchTopChange = {
            if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(-1)) {
                // StaggeredGridLayoutManager刷新时调用scrollToPosition方法存在bug
                // https://www.coder.work/article/591740
                if (recyclerView.layoutManager is StaggeredGridLayoutManager) {
                    recyclerView.adapter?.notifyDataSetChanged()
                } else {
                    recyclerView.scrollToPosition(0)
                }
            }
        }
    }
}

@InverseBindingAdapter(attribute = "loadStatus", event = "loadStatusAttrChanged")
fun RecyclerView.getLoadStatus(): Int {
    val adapter = this.adapter as? LoadMoreBindingRecyclerViewAdapter<*>
    val footerStatus = adapter?.getFooterStatus()
    return footerStatus ?: FooterStatus.STATUS_NONE
}