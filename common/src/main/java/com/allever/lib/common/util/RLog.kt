package com.allever.lib.common.util

import android.util.Log

object RLog {

    private val TAG = RLog::class.java.simpleName

    fun d(msg: String) = d(TAG, msg)

    fun d(tag: String, msg: String?) {
        msg ?: return
        Log.d(tag, msg)
    }

    fun e(msg: String) = e(TAG, msg)

    fun e(tag: String, msg: String?) {
        msg ?: return
        Log.e(tag, msg)
    }


}