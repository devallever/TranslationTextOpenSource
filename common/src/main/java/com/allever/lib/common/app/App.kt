package com.allever.lib.common.app

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication

open class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}