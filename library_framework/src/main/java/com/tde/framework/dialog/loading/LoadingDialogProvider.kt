package com.tde.framework.dialog.loading

import android.content.Context

/**
 * 定制加载中弹框
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
object LoadingDialogProvider {

    private var loadingDialogCreate: LoadingDialogCreate = DefaultLoadingDialog()

    /**
     * 设置加载中弹框创建接口
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-24
     * @param loadingDialogCreate LoadingDialogCreate
     */
    fun setLoadingDialogCreate(loadingDialogCreate: LoadingDialogCreate) {
        LoadingDialogProvider.loadingDialogCreate = loadingDialogCreate
    }

    /**
     * 创建加载中弹框
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-24
     * @param context Context 上下文
     * @param title String? 标题
     * @return Dialog
     */
    fun createLoadingDialog(context: Context, title: String?) = loadingDialogCreate.createLoadingDialog(context, title)


}