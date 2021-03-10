package com.tde.framework.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.tde.framework.R
import com.tde.framework.binding.command.BindingCommand
import com.tde.framework.binding.command.BindingConsumer
import com.tde.framework.widget.switchbutton.SwitchView
import com.tde.framework.widget.switchbutton.SwitchView.OnStateChangedListener
import com.tde.network.core.SimpleLifecycleObserver

/**
 * 通用的settingItem控件   特殊情况请自行定义
 * <p>
 * Date: 2020-12-18
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * @constructor
 *
 * Author: zhuanghongzhan
 */
class SettingItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    /**
     * item 左边文字
     */
    private var title: String? = ""

    /**
     * item右边文字
     */
    private var desc: String? = ""

    /**
     * 是否显示右边小箭头
     */
    private var showArrow: Boolean


    /**
     * 右边图标
     */
    private var rightIcon: Drawable? = null

    /**
     * 是否显示右边小红点
     */
    private var showBadge: Boolean = false

    /**
     * 标题的小图标
     */
    private var titleStartDrawable: Drawable? = null

    /**
     * 是否显示底部分割线
     */
    private var showBottomDivider = false

    /**
     * 右边图标的宽度
     */
    private var rightIconWidth: Int = LayoutParams.WRAP_CONTENT

    /**
     * 右边图标的高度
     */
    private var rightIconHeight: Int = LayoutParams.WRAP_CONTENT

    /**
     * 开关的状态
     */
    private var switchCheck: Boolean = false

    private val tvTitle: TextView
    private val tvDesc: TextView
    private val ivArrow: ImageView
    private val rightGroup: Group //右边的group，如果显示switch的话，则将group隐藏
    private val showSwitch: Boolean
    private val switchView: SwitchView
    val ivRightIcon: ImageView

    private var stateChangeList = mutableListOf<BindingCommand<Boolean>?>()

    /**
     * 开关监听
     */
    private val listener: OnStateChangedListener = object : OnStateChangedListener {
        override fun toggleToOn(view: SwitchView?) {
            stateChangeList.forEach {
                it?.execute(true)
            }
        }

        override fun toggleToOff(view: SwitchView?) {
            stateChangeList.forEach {
                it?.execute(false)
            }
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_setting_item_view_layout, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView)
        title = typedArray.getString(R.styleable.SettingItemView_title) ?: ""
        desc = typedArray.getString(R.styleable.SettingItemView_desc) ?: ""
        showArrow = typedArray.getBoolean(R.styleable.SettingItemView_showArrow, true)
        showBadge = typedArray.getBoolean(R.styleable.SettingItemView_showBadge, false)
        rightIcon = typedArray.getDrawable(R.styleable.SettingItemView_rightIcon)
        titleStartDrawable = typedArray.getDrawable(R.styleable.SettingItemView_titleStartDrawable)
        rightIconWidth = typedArray.getDimensionPixelOffset(
            R.styleable.SettingItemView_rightIconWidth,
            LayoutParams.WRAP_CONTENT
        )
        rightIconHeight = typedArray.getDimensionPixelOffset(
            R.styleable.SettingItemView_rightIconHeight,
            LayoutParams.WRAP_CONTENT
        )

        showSwitch = typedArray.getBoolean(R.styleable.SettingItemView_showSwitch, false)
        switchCheck = typedArray.getBoolean(R.styleable.SettingItemView_switchCheck, false)
        typedArray.recycle()
        tvTitle = findViewById(R.id.setting_item_tv_title)
        tvDesc = findViewById(R.id.setting_item_tv_desc)
        ivArrow = findViewById(R.id.setting_item_iv_arrow)
        ivRightIcon = findViewById(R.id.setting_item_iv_right_icon)
        rightGroup = findViewById(R.id.setting_right_group)
        switchView = findViewById(R.id.setting_item_switchView)
        if (background == null) {
            setBackgroundResource(R.drawable.selector_ffffff_bg)
        }
        initView()
        if (context is FragmentActivity) {
            object : SimpleLifecycleObserver(context) {
                override fun onDestroy() {
                    super.onDestroy()
                    removeAllCommand()
                }
            }
        }
    }

    /**
     * 初始化view
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-18
     */
    private fun initView() {
        tvTitle.text = title
        tvDesc.text = desc
        setTitleStartDrawable(titleStartDrawable)
        if (showSwitch) {
            switchView.visibility = View.VISIBLE
            rightGroup.visibility = View.GONE
            switchView.isOpened = switchCheck
            switchView.addOnStateChangedListener(listener)
        } else {
            switchView.visibility = View.GONE
            rightGroup.visibility = View.VISIBLE
        }

        if (showArrow&&!showSwitch) {
            ivArrow.visibility = View.VISIBLE
        } else {
            ivArrow.visibility = View.GONE
        }
        if (rightIcon != null) {
            ivRightIcon.setImageDrawable(rightIcon)
        }
        setRightIconSize(rightIconWidth, rightIconHeight)


    }


    /**
     * 设置标题的startDrawable
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-04
     * @param drawable Drawable
     */
    private fun setTitleStartDrawable(drawable: Drawable?) {
        if (drawable == null) {
            return
        }
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        tvTitle.setCompoundDrawables(drawable, null, null, null)
    }


    /**
     * 设置右边图标的宽高
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-25
     * @param iconWidth Int? 图标的宽度
     * @param iconHeight Int? 图标的高度
     */
    fun setRightIconSize(iconWidth: Int?, iconHeight: Int?) {
        val lp = ivRightIcon.layoutParams
        if (iconWidth != null) {
            lp.width = iconWidth
            this.rightIconWidth = iconWidth
        }
        if (iconHeight != null) {
            lp.height = iconHeight
            this.rightIconHeight = iconHeight
        }
        ivRightIcon.layoutParams = lp
    }


    /**
     * 设置标题
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-18
     * @param title String
     */
    fun setTitle(title: String?) {
        this.title = title
        tvTitle.text = title ?: ""
    }

    /**
     * 设置描述文案
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-18
     * @param desc String
     */
    fun setDesc(desc: String?) {
        this.desc = desc
        tvDesc.text = desc ?: ""
    }

    /**
     * 设置是否显示右边小箭头
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-18
     * @param showArrow Boolean
     */
    fun setShowArrow(showArrow: Boolean) {
        this.showArrow = showArrow
        if (showArrow) {
            ivArrow.visibility = View.VISIBLE
        } else {
            ivArrow.visibility = View.GONE
        }
    }

    /**
     * 设置右边图标
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-18
     * @param rightIcon Drawable
     */
    fun setRightIcon(rightIcon: Drawable?) {
        this.rightIcon = rightIcon
        ivRightIcon.setImageDrawable(rightIcon)
    }

    /**
     * 设置右边图标的url地址
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-24
     */
    fun setRightIconUrl(imageUrl: String?) {
        if (imageUrl.isNullOrBlank()) {
            return
        }
        Glide.with(this).load(imageUrl).into(ivRightIcon)
    }


    /**
     * 设置开关状态
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @param switchCheck Boolean
     */
    fun setSwitchCheck(switchCheck: Boolean) {
        switchView.isOpened = switchCheck
    }

    /**
     * 获取开关状态
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @return Boolean
     */
    fun getSwitchCheck(): Boolean {
        return switchView.isOpened
    }

    /**
     * 添加开关监听
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @param stateChangeCommand BindingCommand<Boolean>
     */
    fun addSwitchStateChangedCommand(stateChangeCommand: BindingCommand<Boolean>) {
        stateChangeList.add(stateChangeCommand)
    }

    /**
     * 移除开关监听
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-01-05
     * @param stateChangeCommand BindingCommand<Boolean>
     */
    fun removeSwitchStateChangedCommand(stateChangeCommand: BindingCommand<Boolean>) {
        stateChangeList.remove(stateChangeCommand)
    }


    fun removeAllCommand() {
        stateChangeList.clear()
    }
}

/**
 * 动态设置右边icon的图标和缺省图
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-25
 * @receiver SettingItemView
 */
@BindingAdapter(value = ["settingIconUrl", "settingIconPlaceholder"], requireAll = false)
fun SettingItemView.setRightIconUrlAndPlaceholder(imageUrl: String?, placeholder: Drawable?) {
    Glide.with(this).load(imageUrl).placeholder(placeholder).into(ivRightIcon)
}

/**
 * 设置开关状态
 * <p>
 * Author: zhuanghongzhan
 * Date: 2021-01-05
 * @receiver SettingItemView
 * @param switchCheck Boolean?
 */
@BindingAdapter(value = ["switchCheck"])
fun SettingItemView.setSwitchCheckBinding(switchCheck: Boolean?) {
    switchCheck ?: return
    if(switchCheck!=getSwitchCheck()){
        setSwitchCheck(switchCheck)
    }
}

/**
 * 双向绑定
 * <p>
 * Author: zhuanghongzhan
 * Date: 2021-01-05
 * @receiver SettingItemView
 * @return Boolean
 */
@InverseBindingAdapter(attribute = "switchCheck", event = "switchCheckAttrChanged")
fun SettingItemView.getBindingSwitchCheck(): Boolean {
    return getSwitchCheck()
}

@BindingAdapter(value = ["switchCheckAttrChanged"])
fun SettingItemView.setSwitchCheckAttrChanged(listener: InverseBindingListener?) {
    addSwitchStateChangedCommand(BindingCommand<Boolean>(BindingConsumer {
        listener?.onChange()
    }))
}

/**
 * 通过databinding设置开关状态监听
 * <p>
 * Author: zhuanghongzhan
 * Date: 2021-01-05
 * @receiver SettingItemView
 * @param stateChangeCommand BindingCommand<Boolean>
 */
@BindingAdapter(value = ["switchStateChangedCommand"])
fun SettingItemView.setSwitchStateChangedCommand(stateChangeCommand: BindingCommand<Boolean>) {
    addSwitchStateChangedCommand(stateChangeCommand)
}
