package com.tde.framework.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * 禁止滑动的ViewPage
 * <p>
 * Date: 2021-01-13
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
class NoSlidingViewPage : ViewPager {

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        /* return false;//super.onTouchEvent(arg0); */
        return false
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return false
    }

}