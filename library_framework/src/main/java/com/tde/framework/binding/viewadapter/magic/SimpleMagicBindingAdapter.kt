package com.tde.framework.binding.viewadapter.magic

import android.content.Context
import android.util.TypedValue
import androidx.databinding.ObservableList
import com.tde.framework.R
import com.tde.framework.binding.command.BindingCommand
import com.tde.framework.extensions.*
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import java.lang.ref.WeakReference

/**
 * 通用的magicAdapter
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * @property items List<String>?
 * @property navigator CommonNavigator?
 * @property callback WeakReferenceOnListChangedCallback?
 * @property onSelectedChange Function1<[@kotlin.ParameterName] Int, Unit>?
 * @property textColorNormal Int?
 * @property textColorSelect Int?
 * @property textSize Int?
 * @property pagerChangeCommand BindingCommand<Int>?
 * @property itemClickCommand BindingCommand<Int>?
 * @property lineIndicatorWidth Float?
 * @property lineIndicatorHeight Float?
 * @property lineIndicatorRoundRadius Float?
 * @property lineIndicatorMode Int?
 * @property isSelectBold Boolean?
 *
 * Author:
 */
class SimpleMagicBindingAdapter : CommonNavigatorAdapter() {

    private var items: List<String>? = null
    private var navigator: CommonNavigator? = null
    private var callback: WeakReferenceOnListChangedCallback? = null
    private var onSelectedChange: ((index: Int) -> Unit)? = null


    /**
     * 默认字体颜色
     */
    var textColorNormal: Int? = R.color.color_999999

    /**
     * 选中字体颜色
     */
    var textColorSelect: Int? = R.color.color_333333

    /**
     * 字体大小
     */
    var textSize: Int? = 18

    /**
     * 选中的index回调，带切换viewpager属性   外部自行切换
     */
    var pagerChangeCommand: BindingCommand<Int>? = null

    /**
     * 点击了对应index的对调，不切换viewpager
     */
    var itemClickCommand: BindingCommand<Int>? = null

    /**
     * 底部线的宽度
     */
    var lineIndicatorWidth: Float? = 0f

    /**
     * 底部线的高度  线粗
     */
    var lineIndicatorHeight: Float? = 2f

    /**
     * 底部线的圆角
     */
    var lineIndicatorRoundRadius: Float? = 0f

    /**
     * 底部线的显示模式，包括自定义、整个item的宽度、和文本的宽度
     */
    var lineIndicatorMode: Int? = LinePagerIndicator.MODE_WRAP_CONTENT

    /**
     * 选中是否加粗
     */
    var isSelectBold: Boolean? = false


    override fun getIndicator(context: Context): IPagerIndicator {
        val linePagerIndicator = LinePagerIndicator(context)
        linePagerIndicator.mode = lineIndicatorMode ?: LinePagerIndicator.MODE_WRAP_CONTENT
        if (linePagerIndicator.mode == LinePagerIndicator.MODE_EXACTLY) {
            linePagerIndicator.lineWidth = (lineIndicatorWidth ?: 0f).dp()
        }
        linePagerIndicator.lineHeight = (lineIndicatorHeight ?: 2f).dp()
        linePagerIndicator.roundRadius = (lineIndicatorRoundRadius ?: 0f).dp()
        linePagerIndicator.setColors(R.color.color_FFFFFF.color())
        linePagerIndicator.yOffset = 2.dp()

        return linePagerIndicator
    }

    override fun getTitleView(context: Context, index: Int): IPagerTitleView {
        val simplePagerTitleView = SimplePagerTitleView(context)
        simplePagerTitleView.id = R.id.indicator_id
        simplePagerTitleView.normalColor = (textColorNormal ?: R.color.color_999999).color()
        simplePagerTitleView.selectedColor = (textColorSelect ?: R.color.color_333333).color()
        simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize ?: 18).sp())
        simplePagerTitleView.text = items?.get(index)
        simplePagerTitleView.setOnClickListener {
            onSelectedChange?.invoke(index)
        }
        return simplePagerTitleView
    }

    override fun getCount(): Int {
        return items?.size ?: 0
    }


    fun setItems(items: List<String>?) {
        if (this.items === items) {
            return
        }
        if (this.items is ObservableList<String>) {
            (this.items as ObservableList<String>).removeOnListChangedCallback(callback)
            callback = null
        }
        if (items is ObservableList<String>) {
            callback = WeakReferenceOnListChangedCallback(this)
            items.addOnListChangedCallback(callback)
        }
        this.items = items
        notifyDataSetChanged()
    }


    private class WeakReferenceOnListChangedCallback(adapter: SimpleMagicBindingAdapter) :
            ObservableList.OnListChangedCallback<ObservableList<String>>() {
        private val adapterRef = WeakReference(adapter)

        override fun onChanged(sender: ObservableList<String>) {
            adapterRef.get()?.notifyDataSetChanged()
        }

        override fun onItemRangeChanged(sender: ObservableList<String>, positionStart: Int, itemCount: Int) {
            adapterRef.get()?.notifyDataSetChanged()
        }

        override fun onItemRangeInserted(sender: ObservableList<String>, positionStart: Int, itemCount: Int) {
            adapterRef.get()?.notifyDataSetChanged()
        }

        override fun onItemRangeMoved(sender: ObservableList<String>, fromPosition: Int, toPosition: Int, itemCount: Int) {
            adapterRef.get()?.notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(sender: ObservableList<String>, positionStart: Int, itemCount: Int) {
            adapterRef.get()?.notifyDataSetChanged()
        }
    }

    /**
     * 绑定CommonNavigator
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-02-01
     * @param navigator CommonNavigator
     */
    fun bindNavigator(navigator: CommonNavigator) {
        this.navigator = navigator
        navigator.adapter = this
    }

    /**
     * 选择监听
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-02-01
     * @param onSelectedChange Function1<[@kotlin.ParameterName] Int, Unit>
     */
    fun setOnSelectedChange(onSelectedChange: (index: Int) -> Unit) {
        this.onSelectedChange = onSelectedChange
    }
}