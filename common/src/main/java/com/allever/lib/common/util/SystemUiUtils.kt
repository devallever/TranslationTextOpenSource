package com.allever.lib.common.util

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

object SystemUiUtils {

    private const val STATUS_BAR_HEIGHT = "status_bar_height"
    private const val NAVIGATION_BAR_HEIGHT = "navigation_bar_height"

    /**
     * 设置透明状态栏
     */
    fun setStatusBarTransparent(window: Window) {
        // 透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.argb(0, 0, 0, 0)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置全屏显示
     */
    fun setFullScreen(window: Window) {
        // 全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int =
        getXBarHeight(context, STATUS_BAR_HEIGHT)

    /**
     * 获取导航栏栏高度
     */

    fun getNavigationBarHeight(context: Context): Int =
        getXBarHeight(context, NAVIGATION_BAR_HEIGHT)

    private fun getXBarHeight(context: Context, parameterName: String): Int {
        var height = 0
        val resourceId: Int =
            context.resources.getIdentifier(parameterName, "dimen", "android")
        if (resourceId > 0) {
            height = context.resources.getDimensionPixelSize(resourceId)
        }

        return height
    }
}