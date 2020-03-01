package com.allever.app.translation.text.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.allever.app.translation.text.R


import com.allever.lib.common.app.BaseActivity
import com.allever.lib.common.util.FeedbackHelper
import kotlinx.android.synthetic.main.activity_guide.*

class GuideActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        findViewById<View>(R.id.iv_left).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_label).text = getString(R.string.setting_guide)

        btnFeedback.setOnClickListener {
            FeedbackHelper.feedback(this)
        }


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_left -> {
                finish()
            }
        }
    }


    companion object {
        fun start(context: Context) {
            val intent = Intent(context, GuideActivity::class.java)
            context.startActivity(intent)
        }
    }
}