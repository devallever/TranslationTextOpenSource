package com.allever.lib.comment

import android.app.Activity
import android.app.Dialog
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.allever.lib.common.util.DisplayUtils
import com.allever.lib.common.util.Tool
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object CommentHelper {

    private var mAlertDialog: AlertDialog? = null
    
    fun createCommentDialog(activity: Activity?, listener: CommentListener?): Dialog? {
        activity?:return null
        val dialog = CommentDialog(activity)
        dialog.setListener(listener)
        return dialog
    }

    fun create(activity: Activity?, listener: CommentListener?): AlertDialog? {
        activity ?: return null

        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_comment, null)
        val llStar = view.findViewById<LinearLayout>(R.id.llStar)
        val tvComment = view.findViewById<View>(R.id.tvComment)
        val tvRefuse = view.findViewById<View>(R.id.tvRefuse)

        llStar.setOnClickListener {
            Tool.openAppInPlay(activity, activity.packageName)
//            if (!activity.isFinishing) {
//                mAlertDialog?.dismiss()
//            }
            listener?.onComment(mAlertDialog)
        }

        tvComment.setOnClickListener {
            Tool.openAppInPlay(activity, activity.packageName)
//            if (!activity.isFinishing) {
//                mAlertDialog?.dismiss()
//            }
            listener?.onComment(mAlertDialog)
        }

        tvRefuse.setOnClickListener {
//            if (!activity.isFinishing) {
//                mAlertDialog?.dismiss()
//            }
            listener?.onReject(mAlertDialog)
        }

        mAlertDialog = AlertDialog.Builder(activity)
            .setView(view)
//            .setCancelable(false)
            .create()

        mAlertDialog?.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                listener?.onBackPress(mAlertDialog)
                return@setOnKeyListener true
            }

            false
        }

        mAlertDialog?.window?.setLayout(DisplayUtils.dip2px(280), ViewGroup.LayoutParams.WRAP_CONTENT)

//        if (!activity.isFinishing) {
//            mAlertDialog?.show()
//        }

        return mAlertDialog
    }

    fun show(activity: Activity?, dialog: Dialog?) {
        if (activity?.isFinishing == false) {
            dialog?.show()
            dialog?.window?.setLayout(DisplayUtils.dip2px(320), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }
}