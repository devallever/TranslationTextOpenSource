package com.allever.lib.permission

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.core.app.AppOpsManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object FastPermission {

    private val PERMISSION_FRAGMENT_TAG = PermissionFragment::class.java.simpleName

    fun request(
        activity: Activity?,
        listener: PermissionListener?,
        vararg permissions: String
    ) {
        val fragmentActivity = activity as? FragmentActivity
        val fragmentManager = fragmentActivity?.supportFragmentManager
        val fragment = fragmentManager?.findFragmentByTag(PERMISSION_FRAGMENT_TAG)
        val permissionFragment = if (fragment == null) {
            val newFragment =
                PermissionFragment()
            fragmentManager?.beginTransaction()?.add(
                newFragment,
                PERMISSION_FRAGMENT_TAG
            )?.commit()
            newFragment
        } else {
            fragment as? PermissionFragment
        }
        permissionFragment?.request(listener, *permissions)
    }

    fun request(
        fragment: Fragment,
        listener: PermissionListener?,
        vararg permissions: String
    ) {
        val fragmentManager = fragment.fragmentManager
        val fg = fragmentManager?.findFragmentByTag(PERMISSION_FRAGMENT_TAG)
        val permissionFragment = if (fg == null) {
            val newFragment =
                PermissionFragment()
            fragmentManager?.beginTransaction()?.add(
                newFragment,
                PERMISSION_FRAGMENT_TAG
            )
                ?.commitNow()
            newFragment
        } else {
            fg as PermissionFragment
        }
        permissionFragment.request(listener, *permissions)
    }

//    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
//        for (permission in permissions) {
//            val result = ContextCompat.checkSelfPermission(context, permission)
//            if (result == PackageManager.PERMISSION_DENIED) {
//                return false
//            }
//        }
//        return true
//    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        return hasPermissions(
            context,
            listOf(*permissions)
        )
    }

    fun hasPermissions(context: Context, permissions: List<String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        for (permission in permissions) {
            var result = ContextCompat.checkSelfPermission(context, permission)
            if (result == PackageManager.PERMISSION_DENIED) {
                return false
            }

            val op = AppOpsManagerCompat.permissionToOp(permission)
            if (TextUtils.isEmpty(op)) {
                continue
            }
            result = AppOpsManagerCompat.noteProxyOp(context, op!!, context.packageName)
            if (result != AppOpsManagerCompat.MODE_ALLOWED) {
                return false
            }
        }
        return true
    }

    fun hasAlwaysDeniedPermission(activity: Activity, vararg deniedPermissions: String): Boolean {
        return hasAlwaysDeniedPermission(
            activity,
            listOf(*deniedPermissions)
        )
    }

    fun hasAlwaysDeniedPermission(activity: Activity, deniedPermissions: List<String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }

        if (deniedPermissions.isEmpty()) {
            return false
        }

        for (permission in deniedPermissions) {
            val rationale = activity.shouldShowRequestPermissionRationale(permission)
            if (!rationale) {
                return true
            }
        }
        return false
    }

//    fun hasAlwaysDeniedPermission(activity: Activity, permission: String): Boolean {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return false
//        }
//        val rationale = activity.shouldShowRequestPermissionRationale(permission)
//        return !rationale
//    }
//
//    fun hasAlwaysDeny(activity: Activity, vararg permissions: String): Boolean {
//        for (permission in permissions) {
//            val result = !shouldShowRequestPermissionRationale(activity, permission)
//            if (result) {
//                return true
//            }
//        }
//        return false
//    }

    fun openPermissionManually(
        activity: Activity?,
        requestCode: Int,
        cancelListener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.permission_permission_need_some_permission)
        builder.setTitle(R.string.permission_permission_warm_tips)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.permission_permission_go) { dialog, which ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", activity?.packageName, null)
            intent.data = uri
            activity?.startActivityForResult(intent, requestCode)
        }
        builder.setNegativeButton(R.string.permission_permission_cancel, cancelListener)
        builder.show()
    }
}