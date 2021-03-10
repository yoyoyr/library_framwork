package com.tde.framework.binding.viewadapter.magic

import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.tde.framework.binding.command.BindingCommand
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * 指示器绑定
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author:
 */
@BindingMethods(BindingMethod(type = MagicIndicator::class, attribute = "setAdapterCount", method = "setAdapterCount"),
        BindingMethod(type = MagicIndicator::class, attribute = "setCustomAdapter", method = "setCustomAdapter"),
        BindingMethod(type = MagicIndicator::class, attribute = "pagerChangeCommand", method = "pagerChangeCommand"),
        BindingMethod(type = MagicIndicator::class, attribute = "itemBinding", method = "setCommonAdapter"),
        BindingMethod(type = MagicIndicator::class, attribute = "items", method = "setCommonAdapter"),
        BindingMethod(type = MagicIndicator::class, attribute = "onMagicSelectedChange", method = "setCommonAdapter"),
        BindingMethod(type = MagicIndicator::class, attribute = "viewPager", method = "setCommonAdapter"),
        BindingMethod(type = ViewPager::class, attribute = "setCurrentItem", method = "setCurrentItem")
)
class MagicIndicatorViewAdapter

@BindingAdapter(value = ["setCurrentItem"], requireAll = false)
fun ViewPager.setBindingCurrentItem(index: Int) {
    this.currentItem = index
}


/**
 *
 * Desc:DataBinding设置MagicIndicator属性
 * <p>
 * Author: zhuanghongzhan
 * Date: 2021-02-01
 * @receiver MagicIndicator
 * @param mDataList ArrayList<String>  数据集
 * @param pagerChangeCommand BindingCommand<Int>? 选中的index回调，带切换viewpager属性
 * @param itemClickCommand BindingCommand<Int>? 点击了对应index的对调，不切换viewpager
 * @param isAdjustMode Boolean? 设置文本显示模式，自适应模式，适用于数目固定的、少量的title
 * @param selectIndex Int? 设置选中对应的item和viewPager切换
 * @param viewPager ViewPager? 绑定viewPager
 * @param textColorNormal 默认字体颜色
 * @param textColorSelect 选中字体颜色
 * @param textSize 字号
 * @param lineIndicatorWidth Float? 底部线的宽度
 * @param lineIndicatorHeight Float? 底部线的高度  线粗
 * @param lineIndicatorRoundRadius Float? 底部线的圆角
 * @param lineIndicatorMode Int? 底部线的显示模式，包括自定义、整个item的宽度、和文本的宽度
 * @param isSelectBold 是否选中加粗
 */
@BindingAdapter(
        value = ["setAdapterCount",
            "pagerChangeCommand",
            "itemClickCommand",
            "isAdjustMode",
            "selectIndex",
            "viewPager",
            "viewPager2",
            "textColorNormal",
            "textColorSelect",
            "textSize",
            "lineIndicatorWidth",
            "lineIndicatorHeight",
            "lineIndicatorRoundRadius",
            "lineIndicatorMode",
            "fragmentContainerHelper",
            "isSelectBold"],
        requireAll = false
)
fun MagicIndicator.setAdapterCount(
    mDataList: List<String>?,
    pagerChangeCommand: BindingCommand<Int>?,
    itemClickCommand: BindingCommand<Int>?,
    isAdjustMode: Boolean?,
    selectIndex: Int?,
    viewPager: ViewPager?,
    viewPager2: ViewPager2?,
    textColorNormal: Int?,
    textColorSelect: Int?,
    textSize: Int?,
    lineIndicatorWidth: Float?,
    lineIndicatorHeight: Float?,
    lineIndicatorRoundRadius: Float?,
    lineIndicatorMode: Int?,
    fragmentContainerHelper: FragmentContainerHelper?,
    isSelectBold: Boolean?
) {
    mDataList ?: return

    var navigator = this.navigator
    if (navigator !is CommonNavigator) {
        navigator = CommonNavigator(this.context)
        if (isAdjustMode != null) {
            navigator.isAdjustMode = isAdjustMode
        }
    }
    val oldAdapter = navigator.adapter as? SimpleMagicBindingAdapter
    val newAdapter = oldAdapter ?: SimpleMagicBindingAdapter().apply {
        this.pagerChangeCommand = pagerChangeCommand
        this.itemClickCommand = itemClickCommand
        this.textColorNormal = textColorNormal
        this.textColorSelect = textColorSelect
        this.textSize = textSize
        this.lineIndicatorWidth = lineIndicatorWidth
        this.lineIndicatorHeight = lineIndicatorHeight
        this.lineIndicatorRoundRadius = lineIndicatorRoundRadius
        this.lineIndicatorMode = lineIndicatorMode
        this.isSelectBold = isSelectBold
    }
    newAdapter.setItems(mDataList)
    newAdapter.bindNavigator(navigator)
    newAdapter.setOnSelectedChange { index ->
        fragmentContainerHelper?.handlePageSelected(index)
        pagerChangeCommand?.execute(index)
        viewPager?.currentItem = index
        viewPager2?.currentItem = index
    }
    this.navigator = navigator

    if (selectIndex != null && selectIndex >= 0) {
        this.navigator.onPageSelected(selectIndex)
    }
    viewPager?.let {
        ViewPagerHelper.bind(this, viewPager)
        this.onPageSelected(viewPager.currentItem)
    }
    viewPager2?.let {
        this.bindViewPager2(viewPager2)
        this.onPageSelected(viewPager2.currentItem)
    }
    fragmentContainerHelper?.attachMagicIndicator(this)
}



/**
 * Desc: MagicIndicator绑定ViewPager2
 * <p>
 * Author: linjiaqiang
 * Date: 2020/7/6
 */
fun MagicIndicator.bindViewPager2(viewPager2: ViewPager2) {
    var callback = viewPager2.tag as? ViewPager2.OnPageChangeCallback
    if (callback == null) {
        callback = object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                this@bindViewPager2.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                this@bindViewPager2.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                this@bindViewPager2.onPageSelected(position)
            }
        }
        viewPager2.registerOnPageChangeCallback(callback)
    }
}