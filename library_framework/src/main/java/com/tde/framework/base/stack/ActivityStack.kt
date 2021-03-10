package com.tde.framework.base.stack

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import java.util.*


/**
 * activity 任务栈管理
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
object ActivityStack {

    private val activityStacks by lazy { Stack<Activity>() }

    fun getActivityStack() =
        activityStacks

    /**
     * 加入activity
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @param activity Activity
     */
    fun addActivity(activity: Activity) {
        activityStacks.add(activity)
    }

    /**
     * 移除activity
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @param activity Activity
     */
    fun removeActivity(activity: Activity) {
        if (activityStacks.contains(activity)) {
            activityStacks.remove(activity)
        }
    }

    /**
     * 判断是否有activity
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @return Boolean
     */
    fun hasActivity(): Boolean {
        return activityStacks.isEmpty()
    }

    /**
     * 获取当前Activity
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @return Activity
     */
    fun currentActivity(): Activity? {
        return try {
            activityStacks.lastElement()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 关闭activity
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @param activity Activity
     */
    fun finishActivity(activity: Activity) {
        if (!activity.isFinishing) {
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @param cls Class<*>
     */
    fun finishActivity(cls: Class<*>) {
        val activity = activityStacks.firstOrNull { it.javaClass == cls }
        if (activity != null) {
            finishActivity(activity)
        }
    }

    /**
     * 关闭所有的Activity
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     */
    fun finishAllActivity() {
        activityStacks.forEach {
            finishActivity(it)
        }
        activityStacks.clear()
    }


    /**
     * 退出应用程序
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     */
    fun appExit() {
        finishAllActivity()
    }


    /**
     * 判断是否有fragment
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-02-07
     * @param activity FragmentActivity
     * @return Boolean
     */
    fun checkHasFragment(activity: Activity): Boolean {
        if (activity !is FragmentActivity) {
            return false
        }
        val list = activity.supportFragmentManager.fragments
        if (list.size == 0) {
            return false
        }
        if (list.size == 1 && list[0].javaClass.simpleName.contains("SupportRequestManagerFragment")) {
            return false
        }
        return true
    }


}