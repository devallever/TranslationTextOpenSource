package com.allever.app.translation.text.ui.mvp.presenter

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.allever.app.translation.text.R
import com.allever.app.translation.text.ui.mvp.view.MainView
import com.allever.lib.common.mvp.BasePresenter
import com.allever.lib.common.util.toast
import com.allever.lib.permission.PermissionListener
import com.allever.lib.permission.PermissionManager

class MainPresenter : BasePresenter<MainView>() {
    fun requestPermission(activity: Activity) {
        if (!PermissionManager.hasPermissions(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            //弹窗
            AlertDialog.Builder(activity)
                .setMessage(R.string.permission_tips)
                .setPositiveButton(R.string.permission_accept) { dialog, which ->
                    dialog.dismiss()
                    PermissionManager.request(
                        object : PermissionListener {
                            override fun onGranted(grantedList: MutableList<String>) {}

                            override fun onDenied(deniedList: MutableList<String>) {
                                super.onDenied(deniedList)
                                toast(R.string.reject_permission_tips)
                            }

                        }, Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
                .setNegativeButton(R.string.permission_reject) { dialog, which ->
                    dialog.dismiss()
                    toast(R.string.reject_permission_tips)
                }
                .create()
                .show()
        }
    }
}