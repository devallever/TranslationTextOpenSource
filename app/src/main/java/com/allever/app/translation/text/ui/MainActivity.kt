package com.allever.app.translation.text.ui

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.allever.app.translation.text.R
import com.allever.app.translation.text.app.BaseActivity
import com.allever.app.translation.text.ui.mvp.presenter.MainPresenter
import com.allever.app.translation.text.ui.mvp.view.MainView

class MainActivity : BaseActivity<MainView, MainPresenter>(), MainView, View.OnClickListener {


    override fun getContentView(): Any = R.layout.activity_main

    override fun initView() {
        findViewById<TextView>(R.id.tv_label).text = getString(R.string.app_name)
        findViewById<View>(R.id.iv_left).setOnClickListener(this)
        val rightView = findViewById<ImageView>(R.id.iv_right)
        rightView.setOnClickListener(this)
        rightView.setImageResource(R.drawable.ic_setting)
        rightView.visibility = View.VISIBLE
    }

    override fun initData() {
    }

    override fun createPresenter(): MainPresenter = MainPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_left -> {
                finish()
            }
            R.id.iv_right -> {
//                SettingActivity.start(this)
            }

        }
    }

    override fun onBackPressed() {
        checkExit()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun updateResult(result: String) {

    }
}
