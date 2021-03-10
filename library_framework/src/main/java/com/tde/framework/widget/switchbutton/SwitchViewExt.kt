package com.tde.framework.widget.switchbutton

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.tde.framework.R
import com.tde.framework.binding.command.BindingCommand

/**
 *
 * <p>
 * Date: 21-1-18
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: ligangtang
 */
class SwitchViewExt

@BindingAdapter(value = ["switchStateChangedCommand"])
fun setSwitchStateChangedCommand(
    switchView: SwitchView,
    stateChangeCommand: BindingCommand<Boolean>?
) {
    switchView.addOnStateChangedListener(object : SwitchView.OnStateChangedListener {
        override fun toggleToOn(view: SwitchView?) {
            stateChangeCommand?.execute(true)
        }

        override fun toggleToOff(view: SwitchView?) {
            stateChangeCommand?.execute(false)
        }

    })
}


/**
 * 双向绑定
 * <p>
 * Author: zhuanghongzhan
 * Date: 2021-01-05
 * @receiver SettingItemView
 * @return Boolean
 */
@InverseBindingAdapter(attribute = "isOpened", event = "switchViewCheckAttrChanged")
fun SwitchView.getIsOpened(): Boolean {
    return isOpened()
}

@BindingAdapter(value = ["isOpened"])
fun SwitchView.setIsOpened(isOpen: Boolean?) {
    isOpen ?: return
    if (isOpened() == isOpen) {
        return
    }
    setOpened(isOpen)
}

@BindingAdapter(value = ["switchViewCheckAttrChanged"])
fun SwitchView.setSwitchViewCheckAttrChanged(mInverseBindingListener: InverseBindingListener?) {
    var listener =
        getTag(R.id.switch_view_binding_listener_id) as? SwitchView.OnStateChangedListener
    if (listener == null) {
        listener = object : SwitchView.OnStateChangedListener {
            override fun toggleToOn(view: SwitchView?) {
                mInverseBindingListener?.onChange()
            }

            override fun toggleToOff(view: SwitchView?) {
                mInverseBindingListener?.onChange()
            }
        }
        setTag(R.id.switch_view_binding_listener_id, listener)
        addOnStateChangedListener(listener)
    }
}