package com.tde.framework.base.stack

import androidx.fragment.app.Fragment
import com.tde.framework.utils.LoggerUtils
import java.util.*


/**
 *
 * <p>
 * Date: 2021-01-21
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
object FragmentStack {

    private val fragmentStacks by lazy { Stack<Fragment>() }

    fun getFragmentStack() =
        fragmentStacks

    fun addFragment(fragment: Fragment) {
        try {
            if (fragment.javaClass.simpleName.contains("SupportRequestManagerFragment")) {//glide的fragment
                return
            }
            fragmentStacks.add(fragment)
//        LoggerUtils.LOGV("add  $fragment")
            LoggerUtils.LOGV("visibile  ${fragmentStacks.lastElement()}")
        } catch (ex: Exception) {
            LoggerUtils.LOGW(ex)
        }


    }

    fun removeFragment(fragment: Fragment) {
        try {

            if (fragmentStacks.contains(fragment)) {
                fragmentStacks.remove(fragment)
            }
        } catch (ex: Exception) {
            LoggerUtils.LOGW(ex)
        }
    }

    fun hasFragment(): Boolean {
        return fragmentStacks.isEmpty()
    }

    fun currentFragment(): Fragment? {
        try {
            return fragmentStacks.lastElement()
        } catch (ex: Exception) {
            LoggerUtils.LOGW(ex)
            return null
        }
    }

}