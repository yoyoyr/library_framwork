package com.tde.framework.base

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 *
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * @param M : ViewDataBinding
 * @param VM : BaseViewModel<*>
 * @property isViewCreated Boolean
 * @property isVisibleToUser Boolean
 * @property isDataLoaded Boolean
 *
 * Author:
 */
abstract class BaseLazyFragment<M : ViewDataBinding, VM : BaseViewModel<*>> : BaseFragment<M, VM>() {
    private var isViewCreated // 界面是否已创建完成
            = false
    private var isVisibleToUser // 是否对用户可见
            = false
    private var isDataLoaded // 数据是否已请求
            = false

    /**
     * 第一次可见时触发调用,此处实现具体的数据请求逻辑
     */
    protected abstract fun lazyLoadData()

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        tryLoadData()
    }

    /**
     * 保证在initData后触发
     */
    override fun onResume() {
        super.onResume()
        isViewCreated = true
        tryLoadData()
    }

    /**
     * ViewPager场景下，判断父fragment是否可见
     */
    private  fun isParentVisible(): Boolean {
        val fragment: Fragment? = parentFragment
        return fragment == null || fragment is BaseLazyFragment<*, *> && fragment.isVisibleToUser
    }

    /**
     * ViewPager场景下，当前fragment可见时，如果其子fragment也可见，则让子fragment请求数据
     */
    private  fun dispatchParentVisibleState() {
        val fragmentManager: FragmentManager = childFragmentManager
        val fragments: List<Fragment> = fragmentManager.fragments
        if (fragments.isEmpty()) {
            return
        }
        for (child in fragments) {
            if (child is BaseLazyFragment<*, *> && child.isVisibleToUser) {
                child.tryLoadData()
            }
        }
    }

    open fun tryLoadData() {
        if (isViewCreated && isVisibleToUser && isParentVisible() && !isDataLoaded) {
            lazyLoadData()
            isDataLoaded = true
            //通知子Fragment请求数据
            dispatchParentVisibleState()
        }
    }
}