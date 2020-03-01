package com.allever.app.translation.text.ui

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.allever.app.translation.text.BuildConfig
import com.allever.app.translation.text.R
import com.allever.app.translation.text.app.BaseActivity
import com.allever.app.translation.text.ui.mvp.presenter.AboutPresenter
import com.allever.app.translation.text.ui.mvp.view.AboutView
import com.allever.lib.common.app.App
import com.allever.lib.common.util.SystemUtils
import com.allever.lib.umeng.UMeng

class AboutActivity : BaseActivity<AboutView, AboutPresenter>(), AboutView, View.OnClickListener {


    override fun getContentView(): Any = R.layout.activity_about

    override fun initView() {
        findViewById<View>(R.id.about_privacy).setOnClickListener(this)
        findViewById<View>(R.id.iv_left).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_label).text = getString(R.string.about)
        val channel = UMeng.getChannel()
        val last = if (BuildConfig.DEBUG) {
            "(Debug)-$channel"
        } else {
            ""
        }
        findViewById<TextView>(R.id.about_app_version).text = "v${BuildConfig.VERSION_NAME}$last"
        findViewById<TextView>(R.id.about_right).text =
            String.format(getString(R.string.about_right), getString(R.string.app_name))
    }

    override fun initData() {
    }

    override fun createPresenter(): AboutPresenter = AboutPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_left -> {
                finish()
            }
            R.id.about_privacy -> {
                val privacyUrl = "http://x.xiniubaba.com/x.php/rlZSf/3253"
                SystemUtils.startWebView(App.context, privacyUrl)
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AboutActivity::class.java)
            context.startActivity(intent)
        }
    }
}