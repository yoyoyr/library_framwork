package com.tde.framework.extensions

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * 创建viewModel  扩展
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * @param application Application
 * @param create Function0<ViewModel?>?
 * @constructor
 *
 * Author: yangrong
 */
class AndroidViewModelFactory(
        val application: Application,
        var create: (() -> ViewModel?)? = null
) : ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            create?.invoke() as? T ?: super.create(modelClass)
}