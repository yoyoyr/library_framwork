package com.tde.framework.utils

import android.os.Build
import android.text.TextUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * 判断手机ROM,检测ROM是MIUI、EMUI还是Flyme
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: yangrong
 */
@Suppress("SpellCheckingInspection")
object RomUtil {


    const val ROM_MIUI = "MIUI"
    const val ROM_EMUI = "EMUI"
    const val ROM_FLYME = "FLYME"
    const val ROM_OPPO = "OPPO"
    const val ROM_SMARTISAN = "SMARTISAN"
    const val ROM_VIVO = "VIVO"
    const val ROM_QIKU = "QIKU"

    private const val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
    private const val KEY_VERSION_EMUI = "ro.build.version.emui"
    private const val KEY_VERSION_OPPO = "ro.build.version.opporom"
    private const val KEY_VERSION_SMARTISAN = "ro.smartisan.version"
    private const val KEY_VERSION_VIVO = "ro.vivo.os.version"

    private var sName: String? = null
    private var sVersion: String? = null

    //华为
    fun isEmui(): Boolean {
        return check(ROM_EMUI)
    }

    //小米
    fun isMiui(): Boolean {
        return check(ROM_MIUI)
    }

    //vivo
    fun isVivo(): Boolean {
        return check(ROM_VIVO)
    }

    fun isVivo_V2(): Boolean {
        val version = getProp(KEY_VERSION_VIVO)
        return version?.startsWith("2") ?: false
    }

    fun isVivo_V3(): Boolean {
        val version = getProp(KEY_VERSION_VIVO)
        return version?.startsWith("3") ?: false
    }

    fun isOppo(): Boolean {
        return check(ROM_OPPO)
    }

    fun isOppo_V3(): Boolean {
        val version = getProp(KEY_VERSION_OPPO)
        return version?.startsWith("V3") ?: false
    }

    fun isOppo_V2(): Boolean {
        val version = getProp(KEY_VERSION_OPPO)
        return version?.startsWith("V2") ?: false
    }

    //魅族
    fun isFlyme(): Boolean {
        return check(ROM_FLYME)
    }

    //360手机
    fun is360(): Boolean {
        return check(ROM_QIKU) || check("360")
    }

    fun isSmartisan(): Boolean {
        return check(ROM_SMARTISAN)
    }

    fun getName(): String? {
        if (sName == null) {
            check("")
        }
        return sName
    }

    fun getVersion(): String {
        if (sVersion == null) {
            check("")
        }
        return sVersion!!
    }

    private fun check(rom: String): Boolean {
        if (sName != null) {
            return sName == rom
        }
        if (!TextUtils.isEmpty(getProp(KEY_VERSION_MIUI).also { sVersion = it })) {
            sName = ROM_MIUI
        } else if (!TextUtils.isEmpty(getProp(KEY_VERSION_EMUI).also { sVersion = it })) {
            sName = ROM_EMUI
        } else if (!TextUtils.isEmpty(getProp(KEY_VERSION_OPPO).also { sVersion = it })) {
            sName = ROM_OPPO
        } else if (!TextUtils.isEmpty(getProp(KEY_VERSION_VIVO).also { sVersion = it })) {
            sName = ROM_VIVO
        } else if (!TextUtils.isEmpty(getProp(KEY_VERSION_SMARTISAN).also {
                sVersion = it
            })) {
            sName = ROM_SMARTISAN
        } else {
            sVersion = Build.DISPLAY
            if (sVersion?.toUpperCase(Locale.getDefault())?.contains(ROM_FLYME) == true) {
                sName = ROM_FLYME
            } else {
                sVersion = Build.UNKNOWN
                sName = Build.MANUFACTURER.toUpperCase(Locale.getDefault())
            }
        }
        return sName == rom
    }

    private fun getProp(name: String): String? {
        val line: String?
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $name")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return line
    }

}