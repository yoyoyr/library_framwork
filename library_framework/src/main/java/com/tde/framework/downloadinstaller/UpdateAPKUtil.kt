package com.tde.framework.downloadinstaller

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.tde.framework.base.BaseApplication
import com.tde.framework.cache.FrameWorkCache
import com.tde.framework.utils.LoggerUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

const val taskCount = 1
private const val intentType = "application/vnd.android.package-archive"

@Suppress("DEPRECATION")
class UpdateAPKUtil(
    private val downloadUrl: String,
    private val downLoadListener: DownLoadListener
) {

    var loadComplete = false

    suspend fun updateAPK() {
        withContext(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                downLoadListener.onStart()
            }

            var accessFile: RandomAccessFile? = null
            var http: HttpURLConnection? = null
            var totalSize: Long //apk总大小
            val fileInfos = mutableListOf<FileInfoEntity>()
            try {
                val applicationID = BaseApplication.application.packageName
                //防止不同的app 下载同一个链接的App 失败
                val downloadApkUrlMd5 = getUpperMD5Str16(downloadUrl + applicationID)
                val storagePrefix = Environment.getExternalStorageDirectory().path + "/"
                val storageApkPath = "$storagePrefix$downloadApkUrlMd5.apk"
                LoggerUtils.LOGV("path  = $storageApkPath")

                val url = URL(downloadUrl)
                val sizeHttp = url.openConnection() as HttpURLConnection
                sizeHttp.requestMethod = "GET"
                sizeHttp.connect()

                totalSize = sizeHttp.contentLength.toLong()
                sizeHttp.disconnect()

                if (totalSize <= 0) {
                    LoggerUtils.LOGE("文件大小 = $totalSize\t, 终止下载过程")
                    //删除缓存文件和字段
                    FrameWorkCache.getUpdateApkPath(storageApkPath)?.let {
                        File(it).run {
                            delete()
                        }
                        FrameWorkCache.deleteUpdateApkPath(it)
                    }

                    withContext(Dispatchers.Main) {
                        downLoadListener.onFail(NullPointerException("文件大小 = $totalSize\t, 终止下载过程"))
                    }
                    return@withContext
                }

                accessFile = RandomAccessFile(storageApkPath, "rwd")

                //如果是断点续传
//                FrameWorkCache.getUpdateApkPath(storageApkPath)?.run {
//                    JSON.parseArray(FrameWorkCache.getUpdateTaskList(), FileInfoEntity::class.java)
//                        ?.run {
//                            async(Dispatchers.IO) {
//                                forEachIndexed { index, entity ->
//                                    fileInfos.add(entity)
//                                    downloadTask(url, entity, accessFile, index == 0)
//                                }
//                            }.await()
//                            withContext(Dispatchers.Main) {
//                                downLoadListener.onComplete()
//                                loadComplete = true
//                            }
//                            installApk(storageApkPath)
//                            return@withContext
//                        }
//                }
                //第一次下载
                async(Dispatchers.IO) {
                    for (index in 1..taskCount) {
                        val fileInfo = FileInfoEntity(
                            storageApkPath,
                            downloadUrl,
                            totalSize,
                            0,
                            if (index == 1) 0 else (totalSize / taskCount * (index - 1)) + 1,
                            if (index == taskCount) totalSize else (totalSize / taskCount * index)
                        )
                        fileInfos.add(fileInfo)
                        launch {
                            accessFile.seek(fileInfo.from)
                            downloadTask(url, fileInfo, accessFile, index == 1)
                        }
                    }
                }.await()
                withContext(Dispatchers.Main) {
                    downLoadListener.onComplete()
                    loadComplete = true
                }
                installApk(storageApkPath)
            } catch (ex: Exception) {
                LoggerUtils.LOGW(ex)
                withContext(Dispatchers.Main) {
                    downLoadListener.onFail(ex)
                }
            } finally {
//                if (loadComplete) {
//                    FrameWorkCache.deleteUpdateTaskList()
//                } else {
//                    FrameWorkCache.saveUpdateTaskList(JSON.toJSONString(fileInfos))
//                }
                try {
                    accessFile?.close()
                    http?.disconnect()
                } catch (e: IOException) {
                    LoggerUtils.LOGW(e)
                }
            }
        }
    }

    fun installApk(storageApkPath: String) {
        val apkFile = File(storageApkPath)
        if (!apkFile.exists()) {
            return
        }

        val authority = BaseApplication.application.packageName + ".fileProvider"
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val contentUri = FileProvider.getUriForFile(
                BaseApplication.application,
                authority,
                apkFile
            )
            intent.setDataAndType(contentUri, intentType)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            intent.setDataAndType(Uri.parse("file://$apkFile"), intentType)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        BaseApplication.application.startActivity(intent)
        /**
         * 开始安装了
         */
        downLoadListener.onInstall()
    }

    private suspend fun downloadTask(
        url: URL,
        fileInfoEntity: FileInfoEntity,
        accessFile: RandomAccessFile,
        showProgress: Boolean
    ) {
        var http: HttpURLConnection? = null
        var inStream: InputStream? = null
        try {
            LoggerUtils.LOGV("entity = $fileInfoEntity ,showProgress = $showProgress")
            http = url.openConnection() as HttpURLConnection
            http.connectTimeout = 10000
            http.setRequestProperty("Connection", "Keep-Alive")
            http.readTimeout = 10000
            http.setRequestProperty(
                "Range",
                "bytes=${fileInfoEntity.from}-${fileInfoEntity.to}"
            )
            http.connect()

            inStream = http.inputStream
            val buffer = ByteArray(8 * 1024)
            var offset: Int
            val downloadSize = BigDecimal((fileInfoEntity.to - fileInfoEntity.from))
            while (inStream.read(buffer, 0, buffer.size).also { offset = it } != -1) {
                accessFile.write(buffer, 0, offset)
                fileInfoEntity.downLoadPosition += offset.toLong()
                if (showProgress) {

                    downLoadListener.onDownLoading(
                        BigDecimal(fileInfoEntity.downLoadPosition).divide(
                            downloadSize,
                            2,
                            RoundingMode.HALF_UP
                        ).toDouble()
                    )
                }
            }
        } catch (ex: Exception) {
            LoggerUtils.LOGW(ex)
        } finally {
            try {
                inStream?.close()
                http?.disconnect()
            } catch (e: IOException) {
                LoggerUtils.LOGE(e)
            }
        }
    }

    /**
     * 计算md5，为apk的缓存路径之一
     * Author: yangrong
     * Date: 2020-12-28
     * @param str String
     * @return String?
     */
    private fun getUpperMD5Str16(str: String): String? {
        try {
            val messageDigest = MessageDigest.getInstance("MD5")
            messageDigest.reset()
            messageDigest.update(str.toByteArray(charset("UTF-8")))
            val byteArray = messageDigest.digest()
            val md5StrBuff = StringBuffer()
            for (i in byteArray.indices) {
                if (Integer.toHexString(0xFF and byteArray[i].toInt()).length == 1) md5StrBuff.append(
                    "0"
                ).append(
                    Integer.toHexString(0xFF and byteArray[i].toInt())
                ) else md5StrBuff.append(
                    Integer.toHexString(
                        0xFF and byteArray[i]
                            .toInt()
                    )
                )
            }
            return md5StrBuff.toString().toUpperCase().substring(8, 24)
        } catch (e: Exception) {
            LoggerUtils.LOGE(e)
        }
        return null

    }

}