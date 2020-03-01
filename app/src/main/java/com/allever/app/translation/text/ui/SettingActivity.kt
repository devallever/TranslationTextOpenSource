package com.allever.app.translation.text.ui

import android.view.View
import com.allever.app.translation.text.R
import com.allever.app.translation.text.app.BaseActivity
import com.allever.app.translation.text.ui.mvp.presenter.SettingPresenter
import com.allever.app.translation.text.ui.mvp.view.SettingView
import kotlinx.android.synthetic.main.include_top_bar.*

class SettingActivity : BaseActivity<SettingView, SettingPresenter>(), SettingView,
    View.OnClickListener {
    override fun getContentView(): Any = R.layout.activity_setting

    override fun initView() {
        tv_label.text = getString(R.string.setting)
        iv_left.setOnClickListener(this)
    }

    override fun initData() {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, SettingFragment.newInstance())
        transaction.commit()
    }

    override fun createPresenter(): SettingPresenter = SettingPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_left -> {
                finish()
            }
        }
    }
}