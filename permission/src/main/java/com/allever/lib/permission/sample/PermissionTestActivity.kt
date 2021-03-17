package com.allever.lib.permission.sample

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.allever.lib.common.util.DLog
import com.allever.lib.common.util.ToastUtils
import com.allever.lib.permission.PermissionListener
import com.allever.lib.permission.PermissionManager
import com.allever.lib.permission.R

class PermissionTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_test)
        PermissionManager.request(object : PermissionListener {
            override fun onGranted(grantedList: MutableList<String>) {
                ToastUtils.show("onGranted")
                DLog.d("onGranted")
            }

            override fun onDenied(deniedList: MutableList<String>) {
                ToastUtils.show("onDenied")
                DLog.d("onDenied")
            }

            override fun alwaysDenied(deniedList: MutableList<String>) {
                DLog.d("alwaysDenied")
            }

        }, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}