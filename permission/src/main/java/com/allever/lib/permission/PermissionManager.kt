package com.allever.lib.permission

import android.app.Activity
import android.content.DialogInterface
import com.allever.lib.common.app.App
import com.yanzhenjie.permission.AndPermission

object PermissionManager {
    fun request(listener: PermissionListener, vararg permissions: String) {
        AndPermission.with(App.context)
            .runtime()
            .permission(permissions)
            .onGranted {
                listener.onGranted(it)
            }
            .onDenied {
                if (AndPermission.hasAlwaysDeniedPermission(App.context, it)) {
                    listener.alwaysDenied(it)
                } else {
                    listener.onDenied(it)
                }
            }
            .start()
    }

//    fun request(
//        activity: Activity?,
//        listener: PermissionListener,
//        vararg permissions: String
//    ) {
//        FastPermission.request(activity, listener, *permissions)
//    }
//
//    fun request(fragment: Fragment, listener: PermissionListener, vararg permissions: String) {
//        FastPermission.request(fragment, listener, *permissions)
//    }

    fun hasPermissions(vararg permissions: String): Boolean {
        return PermissionCompat.hasPermission(App.context, *permissions)
    }

    fun alwaysDenyPermissions(activity: Activity, vararg permissions: String): Boolean =
        AndPermission.hasAlwaysDeniedPermission(activity, *permissions)

    fun jumpPermissionSetting(
        activity: Activity?,
        requestCode: Int,
        cancelListener: DialogInterface.OnClickListener
    ) {
        FastPermission.openPermissionManually(activity, requestCode, cancelListener)
    }
}