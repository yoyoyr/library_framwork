package com.tde.framework.downloadinstaller

import java.lang.Exception

interface DownLoadListener {
    fun onStart()
    fun onDownLoading(percent: Double)
    fun onInstall()
    fun onComplete()
    fun onFail(ex:Exception?)
}