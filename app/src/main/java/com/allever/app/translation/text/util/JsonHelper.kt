package com.allever.app.translation.text.util

import com.google.gson.Gson

object JsonHelper {
    /**
     * 将字符串转换为 对象
     *
     * @param json
     * @param type
     * @return
     */
    fun <T> json2Object(json: String, type: Class<T>): T? {
        return try {
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun object2Json(any: Any): String? {
        return try {
            Gson().toJson(any)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}