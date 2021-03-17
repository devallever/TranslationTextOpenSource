package com.allever.lib.comment

import android.app.Dialog
import androidx.appcompat.app.AlertDialog

interface CommentListener {

    fun onComment(dialog: Dialog?)

    fun onReject(dialog: Dialog?)

    fun onBackPress(dialog: Dialog?)
}