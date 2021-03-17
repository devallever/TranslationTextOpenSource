package com.allever.lib.common.util.log

interface ILogger {
    fun d(tag: String, msg: String)
    fun d(msg: String)
    fun e(tag: String, msg: String)
    fun e(msg: String)
}