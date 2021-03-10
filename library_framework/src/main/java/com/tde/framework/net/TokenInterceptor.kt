package com.tde.framework.net

import com.alibaba.fastjson.JSON
import com.tde.framework.cache.FrameWorkCache
import com.tde.framework.cache.FrameWorkCache.ACCESS_TOKEN
import com.tde.framework.utils.LoggerUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.jvm.Throws


/**
 *
 * <p>
 * Author: yangrong
 * @description:
 * @date :2020/12/23 11:22
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
const val REFRESH_TOKEN_API = "/api/app/refreshAccessToken"

class TokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        //判断token过期
        if (isTokenExpired(response)) {
            //同步请求方获取最新的Token
            response.close()
            val newToken = getNewToken(chain, request)
            //使用新的Token，创建新的请求
            val newRequest = request
                .newBuilder()
                .header(ACCESS_TOKEN, newToken)
                .build()
            //重新请求
            return chain.proceed(newRequest)
        }
        return response
    }

    /**
     * 根据Response，判断Token是否失效
     */
    private fun isTokenExpired(response: Response): Boolean {
        return response.code == 620 || response.code == 631
    }

    /**
     * 同步请求方式，获取最新的Token
     * 此处需考虑并发问题，多请求时可能同时去刷新token导致刚获取的token马上失效
     */
    @Synchronized
    @Throws(IOException::class)
    private fun getNewToken(
        chain: Interceptor.Chain,
        request: Request
    ): String {
        // 通过获取token的接口，同步请求接口

        val newRequest = request.newBuilder()
            .post("".toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()))
            .url("${request.url.scheme}://${request.url.host}:${request.url.port}$REFRESH_TOKEN_API")
            .build()

        val response = chain.proceed(newRequest)
        var accessToken = FrameWorkCache.getAccessToken()
        val body = response.body?.string()
        if (response.code == 200) {
            val bodyObject = JSON.parseObject(body)
            LoggerUtils.LOGV("code = ${bodyObject.getInteger("code")}")
            if (bodyObject.getInteger("code") == 200) {
                accessToken = bodyObject
                    .getJSONObject("data")[accessToken].toString()
                FrameWorkCache.saveAccessToken(accessToken)
            }

        }
        return accessToken
    }
}