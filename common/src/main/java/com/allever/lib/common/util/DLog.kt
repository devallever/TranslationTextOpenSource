package com.allever.lib.common.util

import android.util.Log
import com.allever.lib.common.BuildConfig

object DLog {

    private val TAG = DLog::class.java.simpleName


    fun d(msg: String = "") {
        d(TAG, msg)
    }

    fun d(tag: String, msg: String = "") {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun e(msg: String) {
        e(TAG, msg)
    }

    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }
}