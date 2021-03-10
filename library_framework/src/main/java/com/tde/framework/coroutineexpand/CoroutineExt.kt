package com.tde.framework.coroutineexpand

import com.tde.framework.base.model.BaseScopeModel
import com.tde.framework.base.model.modelScope
import com.tde.network.core.safeLaunch
import kotlinx.coroutines.CoroutineScope


/**
 * Desc:协程扩展类
 * <p>
 * Author: yangrong
 * Date: 2021-03-10
 * @receiver BaseScopeModel
 * @param block SuspendFunction1<[@kotlin.ParameterName] CoroutineScope, Unit>
 * @param fail Function1<[@kotlin.ParameterName] Throwable, Unit>
 * @return Job
 */
fun BaseScopeModel.launch(block: suspend (coroutineScope: CoroutineScope) -> Unit,
                          fail: (t: Throwable) -> Unit = { }) =
        modelScope.safeLaunch(block, fail)

