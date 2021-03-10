package com.tde.framework.base


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment


//1。第5个页面直接跳到第1个页面，会暂停页面2 3 4。可见-不可见 暂停。不可见-可见 加载
//2。Fragment跳到新的Activity，然后在回来：onPause() onResume()
//3。多层嵌套Fragment时，对Fragment设置show/hide时，嵌套层的Fragment是不会走onHiddenChanged
@Suppress("DEPRECATION")
abstract class VisibleCallbackDialogFragment : DialogFragment() {
    protected abstract fun onFragmentUnVisible()
    protected abstract fun onFragmentVisible()
    private var isViewCreated = false //是否调用了onCreateView()了

    //是否可见。加载或者停止加载数据是由可见到不可见瞬间决定的 (例如，第5个页面直接跳到第1个页面，会暂停页面2 3 4)
    private var isCurrentVisible = false

    override fun onResume() {
        super.onResume()
        if (!isCurrentVisible && userVisibleHint) {
            dispatchUserVisible(true)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isCurrentVisible && userVisibleHint) {
            dispatchUserVisible(false)
        }
    }

    fun dispatchUserVisible(isVisible: Boolean) {
        isCurrentVisible = isVisible
        if (isVisible) {
            onFragmentVisible()
        } else {
            onFragmentUnVisible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false
        isCurrentVisible = false
    }

    /**
     * 多层嵌套Fragment时，对Fragment设置show/hide时，嵌套层的Fragment是不会走onHiddenChanged，
     * 需要上层主动调用嵌套层
     *
     * @param hidden
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isCurrentVisible = hidden
        if (hidden) {
            onFragmentUnVisible()
        } else {
            onFragmentVisible()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isViewCreated = true
        if (userVisibleHint) {
            userVisibleHint = true
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isViewCreated) {
            if (isVisibleToUser && !isCurrentVisible) { //可见--不可见
                dispatchUserVisible(true)
            } else if (!isVisibleToUser && isCurrentVisible) { //不可见--可见
                dispatchUserVisible(false)
            }
        }
    }

}