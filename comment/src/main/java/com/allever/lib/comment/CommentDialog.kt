package com.allever.lib.comment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.allever.lib.common.util.DisplayUtils
import com.allever.lib.common.util.Tool


/**
 * description:自定义dialog
 *
 * @author allever
 */

class CommentDialog(context: Context) : Dialog(context, R.style.CommonCustomDialogStyle) {
    
    private var mListener: CommentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_comment)
        //初始化界面控件
        initView()
    }

    /**
     * 初始化界面控件
     */
    private fun initView() {
        val llStar = findViewById<LinearLayout>(R.id.llStar)
        val tvComment = findViewById<View>(R.id.tvComment)
        val tvRefuse = findViewById<View>(R.id.tvRefuse)

        llStar.setOnClickListener {
            Tool.openAppInPlay(context, context.packageName)
            mListener?.onComment(this)
        }

        tvComment.setOnClickListener {
            Tool.openAppInPlay(context, context.packageName)
            mListener?.onComment(this)
        }

        tvRefuse.setOnClickListener {
            mListener?.onReject(this)
        }
    }

    fun setListener(listener: CommentListener?) {
        mListener = listener
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mListener?.onBackPress(this)
            return true
        }

        return false
    }

    override fun show() {
        super.show()
        window?.setLayout(DisplayUtils.dip2px(320), ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
