package com.tde.framework.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyBoardUtils {
    /**
     * 如果输入法在窗口上已经显示，则隐藏，反之则显示
     */
    @JvmStatic
    fun show(context: Context) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)

    }

    /**
     * 强制显示键盘
     *
     * @param view
     */
    @JvmStatic
    fun forceShow(view: View) {
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)

    }

    /**
     * 强制显示键盘
     *
     * @param view
     */
    @JvmStatic
    fun forceHide(view: View) {
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view.windowToken != null && imm.isActive) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } else {
            Log.w("KeyBoardUtils", "something error!")
        }
    }

    /**
     * 隐藏系统默认的输入法
     *
     * @param activity
     */
    @JvmStatic
    fun hideSystemDefault(activity: Activity) {
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (activity.currentFocus != null && activity.currentFocus!!.windowToken != null) {
            val view = activity.currentFocus
            if (view != null && activity.currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }

    @JvmStatic
    fun hideKeyboard(activity: Activity) {
        val imm =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 判断键盘状态
     *
     * @return 若返回true，则表示输入法打开
     */
    @JvmStatic
    fun isShowForkeyboard(context: Context): Boolean {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.isActive

    }

    @JvmStatic
    fun hideKeyboard(
        event: MotionEvent,
        view: View,
        context: Context
    ) {
        try {
            if (view is EditText) {
                val location = intArrayOf(0, 0)
                view.getLocationInWindow(location)
                val left = location[0]
                val top = location[1]
                val right = (left
                        + view.getWidth())
                val bootom = top + view.getHeight()
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.rawX < left || event.rawX > right || event.y < top || event.rawY > bootom
                ) {
                    // 隐藏键盘
                    val token = view.getWindowToken()
                    val inputMethodManager = context
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(
                        token,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}