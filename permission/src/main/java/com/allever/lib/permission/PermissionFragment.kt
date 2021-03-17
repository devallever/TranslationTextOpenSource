package com.allever.lib.permission

import android.content.pm.PackageManager
import android.util.Log
import androidx.fragment.app.Fragment

class PermissionFragment : Fragment() {

    companion object {
        private const val REQUEST_CODE_PERMISSION = 1
    }

    private var listener: PermissionListener? = null

    fun request(listener: PermissionListener?, vararg permissions: String) {
        this.listener = listener
        val denyList = mutableListOf<String>()
        permissions.map {
            if (!FastPermission.hasPermissions(activity!!, it)) {
                denyList.add(it)
            }
        }
        if (denyList.isEmpty()) {
            log("全部授权")
            listener?.onGranted(permissions.toMutableList())
        } else {
            requestPermissions(denyList.toTypedArray(),
                REQUEST_CODE_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != REQUEST_CODE_PERMISSION) {
            return
        }

        val denyPermissions = mutableListOf<String>()
        val alwaysDenyPermissions = mutableListOf<String>()
        grantResults.mapIndexed { index, it ->
            if (it != PackageManager.PERMISSION_GRANTED) {
                val denyPermission = permissions[index]
                denyPermissions.add(denyPermission)
                if (!shouldShowRequestPermissionRationale(denyPermission)) {
                    alwaysDenyPermissions.add(denyPermission)
                }
            }
        }

        if (denyPermissions.isEmpty()) {
            //全部授权
            log("全部授权")
            listener?.onGranted(permissions.toMutableList())
        } else {
            //拒绝
            if (alwaysDenyPermissions.isNotEmpty()) {
                log("总是拒绝权限，不再弹出")
                listener?.alwaysDenied(alwaysDenyPermissions)
            } else {
                log("拒绝权限")
                listener?.onDenied(denyPermissions)
            }
        }

    }

    private fun log(msg: String) {
        Log.d("FastPermission", msg)
    }
}