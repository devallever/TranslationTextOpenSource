package com.allever.lib.common.app

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.allever.lib.common.R
import com.allever.lib.common.util.DLog
import com.allever.lib.common.util.ActivityCollector
import com.allever.lib.common.util.toast
import java.lang.ref.WeakReference

abstract class BaseActivity : AppCompatActivity() {
    protected val mHandler = Handler(Looper.getMainLooper())
    private var mWeakRefActivity: WeakReference<Activity>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DLog.d(this.javaClass.simpleName)
        mWeakRefActivity = WeakReference(this)
        ActivityCollector.add(mWeakRefActivity)
    }

    override fun onDestroy() {
        ActivityCollector.remove(mWeakRefActivity)
        super.onDestroy()
    }

    private var firstPressedBackTime = 0L
    protected fun checkExit(runnable: Runnable? = null) {
        if (System.currentTimeMillis() - firstPressedBackTime < 2000) {
            runnable?.run()
            super.onBackPressed()
        } else {
            toast(getString(R.string.common_click_again_to_exit))
            firstPressedBackTime = System.currentTimeMillis()
        }
    }

    protected fun setVisibility(view: View, show: Boolean) {
        if (show) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}