package com.allever.app.translation.text.util

import android.os.Build

object CommonHelper {
    fun isAboveAndrodQ(): Boolean {
        if (Build.VERSION.SDK_INT >= 29) {
            return true
        }
        return false
    }
}