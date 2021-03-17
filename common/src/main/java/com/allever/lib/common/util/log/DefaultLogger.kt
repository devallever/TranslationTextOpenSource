package com.allever.lib.common.util.log

import android.util.Log

class DefaultLogger : ILogger {
    companion object {
        private const val TAG = "ILogger"
    }

    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    override fun d(msg: String) {
        d(TAG, msg)
    }

    override fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    override fun e(msg: String) {
        e(TAG, msg)
    }
}