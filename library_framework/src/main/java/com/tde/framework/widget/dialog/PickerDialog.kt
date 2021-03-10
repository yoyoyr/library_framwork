package com.tde.framework.widget.dialog

import android.os.Bundle
import android.view.Gravity
import com.tde.framework.BR
import com.tde.framework.R
import com.tde.framework.base.BaseDialogFragment
import com.tde.framework.base.viewmodel.SpaceViewModel
import com.tde.framework.databinding.PickerBottomDialogBinding
import com.tde.framework.extensions.idp
import com.tde.framework.utils.LoggerUtils
import com.tde.framework.utils.ScreenUtils
import com.tde.framework.widget.NumberPickerView

class PickerDialog : BaseDialogFragment<PickerBottomDialogBinding, SpaceViewModel>(),
    NumberPickerView.OnValueChangeListener {

    override fun getPageName() = "时间范围选择"
    val datas: ArrayList<String>? by lazy {
        arguments?.run {
            getStringArrayList("datas")
        }
    }

    var listener: PickerClickListener? = null
    var selectIndex = 0

    override fun getLayoutId() = R.layout.picker_bottom_dialog

    override fun initVariableId() = BR.viewModel

    override fun initView() {
        super.initView()
        binding.picker.refreshByNewDisplayedValues(datas?.toTypedArray())
        binding.picker.setOnValueChangedListener(this)
        LoggerUtils.LOGV("datas = $datas")
        binding.tvOk.setOnClickListener {
            listener?.onOkClick(selectIndex)
            dismissAllowingStateLoss()
        }
        binding.tvCancel.setOnClickListener {
            listener?.onCancelClick()
            dismissAllowingStateLoss()
        }
    }


    override fun onStart() {
        super.onStart()
        val lp = dialog?.window?.attributes
        if (lp != null) {
            lp.width = ScreenUtils.getScreenWidth()
            lp.height = 200.idp()
            lp.gravity = Gravity.BOTTOM
            dialog?.window?.attributes = lp
        }
    }

    fun setPickerClickListenerr(listener: PickerClickListener) {
        this.listener = listener
    }


    companion object {

        fun newInstance(datas: ArrayList<String>?): PickerDialog {
            val pickerDialog = PickerDialog()
            val bundle = Bundle()
            bundle.putStringArrayList("datas", datas)
            pickerDialog.arguments = bundle
            return pickerDialog
        }
    }

    interface PickerClickListener {
        fun onOkClick(index: Int)
        fun onCancelClick()
    }

    override fun onValueChange(picker: NumberPickerView?, oldVal: Int, newVal: Int) {
        selectIndex = newVal
    }
}