package com.allever.app.translation.text.app

import android.content.ClipboardManager
import android.content.Context
import com.allever.app.translation.text.BuildConfig
import com.allever.app.translation.text.function.SettingHelper
import com.allever.app.translation.text.ui.DialogTranslateActivity
import com.allever.lib.common.app.App
import com.allever.lib.common.util.ActivityCollector
import com.allever.lib.common.util.log
import com.allever.lib.recommend.RecommendGlobal
import com.allever.lib.umeng.UMeng
import org.litepal.LitePal

class MyApp : App() {
    override fun onCreate() {
        super.onCreate()

        com.android.absbase.App.setContext(this)

        //初始化友盟
        if (!BuildConfig.DEBUG) {
            UMeng.init(this)
        }

        Global.initLanguage()

        LitePal.initialize(this)

        val clipBoardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipBoardManager.addPrimaryClipChangedListener {
            log("剪贴板变化")
            if (ActivityCollector.size() == 0 && SettingHelper.getAutoTranslate()) {
                val srcText = clipBoardManager.primaryClip?.getItemAt(0)?.text.toString()
                DialogTranslateActivity.start(this, srcText)
            }
        }

        RecommendGlobal.init(UMeng.getChannel())
    }
}