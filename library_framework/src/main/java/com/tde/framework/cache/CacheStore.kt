package com.tde.framework.cache

import android.content.Context
import android.os.Parcelable
import com.tde.framework.utils.LoggerUtils
import com.tencent.mmkv.MMKV
import java.io.File
import java.lang.Exception

/**
 *
 * TODO
 * 清除文件所有内容？？
 * 读取文件所有内容？？？
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * @param mmkv MMKV
 * @constructor
 *
 * Author: yangrong
 */
class CacheStore private constructor(private val mmkv: MMKV) {


    companion object {
        fun init(application: Context) {
            MMKV.initialize(application)
        }

        fun getDefault(): CacheStore {
            return CacheStore(MMKV.defaultMMKV())
        }

        fun getCacheStoreWithKey(key: String): CacheStore {
            return CacheStore(MMKV.mmkvWithID(key))
        }

        fun getCacheStoreMulProcess(key: String): CacheStore {
            return CacheStore(MMKV.mmkvWithID(key, MMKV.MULTI_PROCESS_MODE))
        }
    }


    fun write(key: String, value: Any): Boolean {
        when (value) {
            is String -> {
                return mmkv.encode(key, value)
            }
            is Int -> {
                return mmkv.encode(key, value)
            }
            is Boolean -> {
                return mmkv.encode(key, value)
            }
            is Float -> {
                return mmkv.encode(key, value)
            }
            is Long -> {
                return mmkv.encode(key, value)
            }
            is Double -> {
                return mmkv.encode(key, value)
            }
            is ByteArray -> {
                return mmkv.encode(key, value)
            }
//            is Set<String> -> {
//               return  mmkv.encode(key, value)
//            }
            is Parcelable -> {
                return mmkv.encode(key, value)
            }
            else -> {
                return mmkv.encode(key, value.toString())
            }
        }
    }

    fun readBool(key: String, default: Boolean = false): Boolean {
        return mmkv.decodeBool(key, default)
    }

    fun readBytes(key: String): ByteArray? {
        return mmkv.decodeBytes(key)
    }

    fun readDouble(key: String): Double {
        return mmkv.decodeDouble(key)
    }

    fun readDouble(key: String, default: Double): Double {
        return mmkv.decodeDouble(key, default)
    }

    fun readFloat(key: String): Float {
        return mmkv.decodeFloat(key)
    }

    fun readInt(key: String): Int {
        return mmkv.decodeInt(key)
    }

    fun readInt(key: String, default: Int): Int {
        return mmkv.decodeInt(key, default)
    }

    fun readLong(key: String): Long {
        return mmkv.decodeLong(key)
    }

    fun readLong(key: String, default: Long): Long {
        return mmkv.decodeLong(key, default)
    }

    fun <T : Parcelable> readParcelable(key: String, cls: Class<T>): T? {
        return mmkv.decodeParcelable(key, cls)
    }

    fun readString(key: String): String? {
        return mmkv.decodeString(key)
    }


    fun readString(key: String, default: String): String {
        return mmkv.decodeString(key, default)
    }

    fun readStringSet(key: String): Set<String>? {
        return mmkv.decodeStringSet(key)
    }

    fun removeWithKey(key: String) {
        mmkv.removeValueForKey(key)
    }

    fun removeWithKeys(keys: Array<String>) {
        mmkv.removeValuesForKeys(keys)
    }

    suspend fun cleanAll(fileName: String) {
        try {
            val file = File(MMKV.getRootDir() + "/$fileName")
//            System.out.println("path ${file.absolutePath}")
            if (file.exists()) {
                file.outputStream().run {
                    write(byteArrayOf())
                    flush()
                    close()
                }
            }
        } catch (ex: Exception) {
            LoggerUtils.LOGW(ex)
        }
    }


    fun clearMemoryCache() {
        mmkv.clearMemoryCache()
    }

    fun getAllKeys(): Array<String> {
        return mmkv.allKeys()
    }


    fun containsKey(key: String): Boolean {
        return mmkv.containsKey(key)
    }
}