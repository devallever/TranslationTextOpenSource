package com.allever.lib.umeng

import android.content.Context
import com.allever.lib.common.app.App
import com.allever.lib.common.util.SystemUtils
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

object UMeng {
    /***
     * init(this):  正式包：读取配置文件的key和channel
     *              调试包：读取配置文件的key和channel默认为debug
     * @param appKey 如果在Manifest配置了，传null
     * @param channel 如果在Manifest配置了，传null
     */
    fun init(context: Context, appKey: String? = null, channel: String? = null, auto: Boolean = true) {
        UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        val appChannel = if (BuildConfig.DEBUG) {
            channel ?: "debug"
        } else {
            channel
        }

        UMConfigure.init(context, appKey, appChannel, UMConfigure.DEVICE_TYPE_PHONE, null)

        if (auto) {
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
        } else {
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL)
        }
    }

    fun getChannel(): String {
        return SystemUtils.getManifestDataByKey(App.context, "UMENG_CHANNEL")
    }
}