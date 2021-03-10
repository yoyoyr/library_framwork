package com.tde.framework.widget.recyclerview.preload

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tde.framework.widget.recyclerview.LoadMoreBindingRecyclerViewAdapter
import com.tde.framework.widget.recyclerview.footer.FooterStatus
import java.util.*

/**
 * 预加载处理
 * <p>
 * Date: 2021-01-14
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * @constructor
 *
 * Author: zhuanghongzhan
 */
internal class PreLoadScrollListener(private val onLoadMoreListener: OnLoadMoreListener, private val preLoadOffset: Int) : RecyclerView.OnScrollListener() {

    /**
     * 存储瀑布流item坐标
     */
    private var staggeredColumnsPositions: IntArray? = null

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy <= 0) {
            return
        }
        val adapter = recyclerView.adapter as? LoadMoreBindingRecyclerViewAdapter<*>
        if (null != adapter) {
            val dataCount = 0.coerceAtLeast(adapter.getBaseItemCount())
            if (dataCount == 0) {
                return
            }
            val layoutManager = recyclerView.layoutManager
            if (null == layoutManager) {
                return
            }
            val lastVisiblePosition = getLastVisiblePosition(recyclerView)
            val visibleItemCount = layoutManager.childCount
            if (visibleItemCount > lastVisiblePosition) {
                return
            }
            val preLoadPosition = adapter.getBaseItemCount() - preLoadOffset
            if (lastVisiblePosition >= preLoadPosition && adapter.canLoadMore() && adapter.getFooterStatus() != FooterStatus.STATUS_FAILED) {
                adapter.setFooterStatus(FooterStatus.STATUS_LOADING)
                onLoadMoreListener.onLoadMore()
            }
        }
    }

    /**
     * 获取最后一个可见item的position
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-14
     * @param recyclerView RecyclerView
     * @return Int
     */
    private fun getLastVisiblePosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager
        var lastVisiblePosition = 0
        try {
            if (layoutManager is LinearLayoutManager) {
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
            } else if (layoutManager is StaggeredGridLayoutManager) {
                val spansCount = layoutManager.spanCount
                if (spansCount == 0) {
                    return lastVisiblePosition
                }
                if (null == staggeredColumnsPositions) {
                    staggeredColumnsPositions = IntArray(spansCount)
                }
                val result = layoutManager.findLastVisibleItemPositions(staggeredColumnsPositions)
                Arrays.sort(result)
                lastVisiblePosition = result[result.size - 1]
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return lastVisiblePosition
    }


}