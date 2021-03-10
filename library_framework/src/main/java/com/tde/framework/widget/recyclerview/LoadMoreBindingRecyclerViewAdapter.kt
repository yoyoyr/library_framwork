package com.tde.framework.widget.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tde.framework.R
import com.tde.framework.databinding.ListFooterBinding
import com.tde.framework.extensions.idp
import com.tde.framework.widget.recyclerview.footer.FooterStatus
import com.tde.framework.widget.recyclerview.footer.FooterStatus.Companion.STATUS_FAILED
import com.tde.framework.widget.recyclerview.footer.FooterStatus.Companion.STATUS_LOADING
import com.tde.framework.widget.recyclerview.footer.FooterStatus.Companion.STATUS_NONE
import com.tde.framework.widget.recyclerview.footer.FooterStatus.Companion.STATUS_NO_MORE
import com.tde.framework.widget.recyclerview.footer.LoadFooter
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

/**
 * 支持加载更多样式的Adapter
 * <p>
 * Date: 2021-01-14
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
open class LoadMoreBindingRecyclerViewAdapter<T> : BindingRecyclerViewAdapter<T>() {

    private lateinit var recyclerView: RecyclerView

    /**
     * 是否可上拉加载
     */
    private var enableLoadMore = true

    /**
     * 是否需要展示无更多数据
     */
    private var enableShowNoMore = true

    /**
     * 加载状态
     */
    private var footerStatus = STATUS_NONE

    /**
     * Footer 底部距离
     */
    private var footerMarginBottom = 0

    /**
     * 点击重试
     */
    internal var retryCall: (() -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        if (hasFooter() && position == itemCount - 1) {
            return R.layout.list_footer
        }
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    override fun getItemId(position: Int): Long {
        if (hasFooter() && position == itemCount - 1) {
            return RecyclerView.NO_ID
        }
        return super.getItemId(position)
    }

    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        if (binding is ListFooterBinding) {
            return BindingViewHolder(binding)
        }
        return super.onCreateViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (hasFooter() && position == itemCount - 1) {
            val binding = DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)
            if (binding is ListFooterBinding) {
                binding.loadFooter.setFooterStatus(footerStatus)
                bindingFooter(binding.loadFooter)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        layoutId: Int,
        viewGroup: ViewGroup
    ): ViewDataBinding {
        val binding = super.onCreateBinding(inflater, layoutId, viewGroup)
        if (binding is ListFooterBinding && footerMarginBottom > 0) {
            binding.loadFooter.setPadding(0, 0, 0, footerMarginBottom.idp())
        }
        return binding
    }

    /**
     * 绑定Footer
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @param loadFooter LoadFooter
     */
    private fun bindingFooter(loadFooter: LoadFooter) {
        loadFooter.retryCall = {
            // 点击重试
            if (canLoadMore()) {
                setFooterStatus(STATUS_LOADING)
                retryCall?.invoke()
            }
        }
    }

    /**
     * 设置Footer状态
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @param status Int
     */
    fun setFooterStatus(@FooterStatus status: Int) {
        if (footerStatus == status) {
            return
        }
        when (status) {
            STATUS_NONE -> {
                if (hasFooter()) {
                    val footerPosition = itemCount - 1
                    footerStatus = status
                    notifyItemRemoved(footerPosition)
                }
            }
            STATUS_NO_MORE -> {
                if (enableShowNoMore && hasFooter()) {
                    // 支持显示无更多数据且当前有footer，notifyItemChanged
                    footerStatus = status
                    notifyItemChanged(itemCount - 1)
                } else if (enableShowNoMore && !hasFooter()) {
                    // 支持显示无更多数据且当前无footer，notifyItemInserted
                    footerStatus = status
                    notifyItemInserted(itemCount - 1)
                } else if (hasFooter()) {
                    // 不支持显示无更多数据且当前有footer，notifyItemRemoved
                    val footerPosition = itemCount - 1
                    footerStatus = status
                    notifyItemRemoved(footerPosition)
                }
            }
            else -> {
                if (hasFooter()) {
                    footerStatus = status
                    notifyItemChanged(itemCount - 1)
                } else {
                    footerStatus = status
                    recyclerView.post {
                        notifyItemInserted(itemCount - 1)
                    }
                }
            }
        }
        footerStatus = status
    }

    /**
     * 获取Footer状态
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @return Int
     */
    fun getFooterStatus(): Int {
        return footerStatus
    }

    /**
     * 是否可上拉加载
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @param loadMoreEnable Boolean
     */
    fun setLoadMoreEnable(loadMoreEnable: Boolean) {
        this.enableLoadMore = loadMoreEnable
    }

    /**
     * 当前是否可加载更多
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @return Boolean
     */
    fun canLoadMore(): Boolean {
        return (footerStatus == STATUS_NONE || footerStatus == STATUS_FAILED) && enableLoadMore
    }

    /**
     * 是否显示Footer
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @return Boolean
     */
    fun hasFooter(): Boolean {
        if (footerStatus == STATUS_LOADING) {
            return true
        }
        if (footerStatus == STATUS_FAILED) {
            return true
        }
        if (footerStatus == STATUS_NO_MORE && enableShowNoMore) {
            return true
        }
        return false
    }

    /**
     *  获取数据ItemCount
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @return Int
     */
    fun getBaseItemCount() = if (hasFooter()) itemCount - 1 else itemCount

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            val defaultSizeLookUp = manager.spanSizeLookup
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position >= getBaseItemCount()) {
                        manager.spanCount
                    } else {
                        defaultSizeLookUp?.getSpanSize(position) ?: 1
                    }
                }
            }
        }
    }

    @CallSuper
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams) {
            if (hasFooter() && holder.itemView is LoadFooter) {
                lp.isFullSpan = true
            }
        }
    }

    /**
     * 是否需要展示无更多数据的样式
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @param enableShowNoMore Boolean
     */
    fun setEnableShowNoMore(enableShowNoMore: Boolean) {
        this.enableShowNoMore = enableShowNoMore
    }

    /**
     * 设置footer的bottom
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @param footerMarginBottom Int
     */
    fun setFooterMarginBottom(footerMarginBottom: Int) {
        this.footerMarginBottom = footerMarginBottom
    }

    private class BindingViewHolder(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root)


}