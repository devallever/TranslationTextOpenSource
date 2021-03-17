package com.allever.lib.common.util

import android.app.Service
import android.util.DisplayMetrics
import android.view.WindowManager
import com.allever.lib.common.app.App

/**
 * Created by allever on 17-8-5.
 * 单位转换工具类
 */

object DisplayUtils {

    private val TAG = "DisplayUtil"

    /**
     * 将px转换成dip或dp
     * @param context
     * @param px 像素值
     * @density 转换比例 例如1dp = 3px, density值就是3
     *
     * 320x480      mdpi    1dp = 1px   density = 1
     * 480x800      hdpi    1dp = 1.5px density = 1.5
     * 720x1280     xhdpi   1dp = 2px   density = 2
     * 1080x1920    xxhdpi  1dp = 3px   density = 3
     * 1440x2560    xxxhdpi 1dp = 4px   density = 4
     *
     * @result 返回dip或dp值
     */
    fun px2dip(px: Int): Int {
        val density = App.context.resources.displayMetrics.density
        val result = (px / density + 0.5f).toInt()
        return result
    }

    fun dip2px(dip: Int): Int {
        val density = App.context.resources.displayMetrics.density
        val result = (dip * density + 0.5f).toInt()
        return result
    }

    fun dip2px(dip: Float): Int {
        val density = App.context.resources.displayMetrics.density
        val result = (dip * density + 0.5f).toInt()
        return result
    }

    fun px2sp(px: Int): Int {
        val scaledDensity = App.context.resources.displayMetrics.scaledDensity
        return (px / scaledDensity + 0.5f).toInt()
    }

    fun sp2px(sp: Int): Int {
        val scaledDensity = App.context.resources.displayMetrics.scaledDensity
        return (sp * scaledDensity + 0.5f).toInt()
    }

    fun getDeviceDensity(): Float {
        return App.context.resources.displayMetrics.density
    }

    fun getDisplayMetrics(): DisplayMetrics {
        return App.context.resources.displayMetrics
    }

    fun getScreenWidth(): Int {
        return getScreenRealMetrics().widthPixels
    }

    fun getScreenHeight(): Int {
        return getScreenRealMetrics().heightPixels
    }

    private fun getScreenRealMetrics(): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        val wm = App.context.getSystemService(Service.WINDOW_SERVICE) as? WindowManager
        wm?.defaultDisplay?.getRealMetrics(displayMetrics)
        return displayMetrics
    }
}
