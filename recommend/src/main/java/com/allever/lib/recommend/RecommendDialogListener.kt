package com.allever.lib.recommend

import android.app.Dialog
import androidx.appcompat.app.AlertDialog

interface RecommendDialogListener {

    fun onMore(dialog: Dialog?)

    fun onReject(dialog: Dialog?)

    fun onBackPress(dialog: Dialog?)
}