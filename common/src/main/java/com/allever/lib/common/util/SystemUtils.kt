package com.allever.lib.common.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.MediaStore
import android.widget.Toast
import com.allever.lib.common.app.App

object SystemUtils {
    /**
     * 从相册中选择一张图片后， 回调Activity的onActivityResult方法进行处理
     */
    fun chooseImageFromGallery(activity: Activity, requestCode: Int) {
        val albumIntent = Intent(Intent.ACTION_PICK)
        albumIntent.type = "image/*"
        albumIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        activity.startActivityForResult(albumIntent, requestCode)
    }

    fun openUrl(activity: Activity, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            log("Launching intent: " + intent + " with extras: " + intent.extras)
            activity.startActivity(intent)
        } catch (ignored: ActivityNotFoundException) {
            log("Nothing available to handle $intent")
        }

    }

    fun isChineseLang(): Boolean {
        val configuration = App.context.resources.configuration
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return configuration.locale.language == "zh"
        } else {
            val langList = configuration.locales
            if (langList.isEmpty) {
                return true
            }
            val lang = langList[0]
            return lang.language == "zh"
        }
    }

    fun startWebView(context: Context, uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Sorry, Your mobile can't be supported", Toast.LENGTH_LONG)
                .show()
        }

    }

    fun getManifestDataByKey(context: Context, key: String): String {
        val appInfo = App.context.packageManager
            .getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
        val msg = appInfo.metaData.getString(key)
        log("$key - $msg")
        return msg?:""
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }

        return statusBarHeight
    }

    @SuppressLint("InvalidWakeLockTag")
    fun wakeUpAndUnlock(context: Context) {
        //屏锁管理器
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val kl = km.newKeyguardLock("unLock")
        //解锁
        kl.disableKeyguard()
        //获取电源管理器对象
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        val wl = pm.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK,
            "bright"
        )
        //点亮屏幕
        wl.acquire(10*60*1000L /*10 minutes*/)
        //释放
        wl.release()
    }

}