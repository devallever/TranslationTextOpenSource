package com.allever.lib.common.util

import android.widget.Toast
import com.allever.lib.common.app.App
import org.jetbrains.anko.runOnUiThread

object ToastUtils {
    fun show(msg: String?) {
        App.context.runOnUiThread {
            Toast.makeText(App.context, msg, Toast.LENGTH_SHORT).show()
        }
        
    }

    fun showLong(msg: String?, duration: Int = Toast.LENGTH_LONG) {
        App.context.runOnUiThread {
            Toast.makeText(App.context, msg, duration).show()
        }
    }
}