package com.tde.framework.binding.command;

/**
 * 带参数的回调
 * <p>
 * Date: 2020-12-16
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * <p>
 * Author: zhuanghongzhan
 */
public interface BindingConsumer<T> {
    void call(T t);
}
