package com.allever.lib.common.util.log

object LogUtils {

    private var mProxy: ILogger = DefaultLogger()

    fun setProxy(logger: ILogger) {
        mProxy = logger
    }

    fun d(tag: String, msg: String) {
        mProxy.d(tag, msg)
    }

    fun d(msg: String) {
        mProxy.d(msg)
    }

    fun e(tag: String, msg: String) {
        mProxy.e(tag, msg)
    }

    fun e(msg: String) {
        mProxy.e(msg)
    }
}