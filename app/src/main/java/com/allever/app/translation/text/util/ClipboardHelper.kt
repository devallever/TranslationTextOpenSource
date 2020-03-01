package com.allever.app.translation.text.util

import com.allever.app.translation.text.R
import com.allever.lib.common.app.App
import com.allever.lib.common.util.getString
import com.allever.lib.common.util.toast

object ClipboardHelper {

    fun copy(content: String?) {
        val ret = ClipboardInterface.setText(content, App.context)
        if (ret) {
            toast(getString(R.string.already_copied_to_clipboard))
        }
    }
}