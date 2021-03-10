package com.tde.framework.base.model

import com.alibaba.fastjson.JSONObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * 基础数据仓库,这边处理RxJava的释放,如果确定不实用rxjava 则这边可以删除
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author:
 */
open class BaseModel : BaseScopeModel() {

    /**
     * map转body
     *
     * @param params
     * @return
     */
    fun getRequestBody(params: Map<String, Any>): RequestBody {
        val jsonObject = JSONObject()
        for ((key, value) in params) {
            jsonObject[key] = value
        }

        val jsonString = jsonObject.toJSONString()
        return jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }
}