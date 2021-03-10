package com.tde.framework.binding.viewadapter.anim

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import com.tde.framework.binding.command.BindingCommand

/**
 * 动画状态监听
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * @property onEnd BindingCommand<Animator>?
 * @property onCancel BindingCommand<Animator>?
 * @property onStart BindingCommand<Animator>?
 * @property onRepeat BindingCommand<Animator>?
 * @property onPause BindingCommand<Animator>?
 * @property onResume BindingCommand<Animator>?
 *
 * Author:
 */
class SimpleAnimatorListenerAdapter : AnimatorListenerAdapter() {

    var onEnd: BindingCommand<Animator>? = null
    var onCancel: BindingCommand<Animator>? = null
    var onStart: BindingCommand<Animator>? = null
    var onRepeat: BindingCommand<Animator>? = null
    var onPause: BindingCommand<Animator>? = null
    var onResume: BindingCommand<Animator>? = null

    override fun onAnimationCancel(animation: Animator) {
        onCancel?.execute(animation)
    }

    override fun onAnimationEnd(animation: Animator) {
        onEnd?.execute(animation)
    }

    override fun onAnimationRepeat(animation: Animator) {
        onRepeat?.execute(animation)

    }

    override fun onAnimationStart(animation: Animator) {
        onStart?.execute(animation)
    }

    override fun onAnimationPause(animation: Animator) {
        onPause?.execute(animation)
    }

    override fun onAnimationResume(animation: Animator) {
        onResume?.execute(animation)
    }


}