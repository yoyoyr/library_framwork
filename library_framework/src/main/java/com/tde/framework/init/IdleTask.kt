package com.tde.framework.init

/**
 *
 * <p>
 * Author: yangrong
 * @description:
 * @date :2020/12/23 17:58
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
internal class IdleTask( n: String, val callback: () -> Unit) : InitManager.IdleHandler(n) {

    override fun idle() {
        callback()
    }
}