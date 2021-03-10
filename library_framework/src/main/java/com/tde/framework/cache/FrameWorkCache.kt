package com.tde.framework.cache

/**
 *
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
object FrameWorkCache {

    private val frameCache by lazy { CacheStore.getCacheStoreWithKey("framework") }

    fun getAccessToken() = frameCache.readString(ACCESS_TOKEN, "")

    fun saveAccessToken(token: String) = frameCache.write(ACCESS_TOKEN, token)

    fun getUpdateApkPath(key: String) = frameCache.readString(key)

    fun saveUpdateApkPath(key: String, path: String) = frameCache.write(key, path)

    fun deleteUpdateApkPath(key: String) = frameCache.removeWithKey(key)

    //记录accessToken
    const val ACCESS_TOKEN = "accessToken"
}