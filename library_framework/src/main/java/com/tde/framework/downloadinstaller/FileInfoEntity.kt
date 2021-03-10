package com.tde.framework.downloadinstaller


data class FileInfoEntity(
    var path: String, //apk缓存路径
    var downLoadUrl: String,//下载地址
    var size: Long,//apk总大小
    var downLoadPosition: Long,//当前下载完成位置
    var from: Long,//下载开始位置
    var to: Long//下载结束位置
)