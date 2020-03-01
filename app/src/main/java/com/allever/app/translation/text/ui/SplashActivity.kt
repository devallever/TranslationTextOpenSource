package com.allever.app.translation.text.ui

import android.os.Bundle
import com.allever.app.translation.text.R
import com.allever.lib.common.app.BaseActivity
import com.allever.lib.common.util.ActivityCollector

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mHandler.postDelayed({
            ActivityCollector.startActivity(this, MainDrawerActivity::class.java)
            finish()
        }, 2000)
    }

    override fun onBackPressed() {

    }
}