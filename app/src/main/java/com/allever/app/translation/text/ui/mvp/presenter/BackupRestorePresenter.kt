package com.allever.app.translation.text.ui.mvp.presenter

import android.Manifest
import android.os.Environment
import com.allever.app.translation.text.R
import com.allever.app.translation.text.bean.BackupBean
import com.allever.app.translation.text.bean.RestoreEvent
import com.allever.app.translation.text.function.db.DBHelper
import com.allever.app.translation.text.function.db.History
import com.allever.app.translation.text.ui.mvp.view.BackupRestoreView
import com.allever.app.translation.text.util.FileUtil
import com.allever.app.translation.text.util.JsonHelper
import com.allever.lib.common.app.App
import com.allever.lib.common.mvp.BasePresenter
import com.allever.lib.common.util.getString
import com.allever.lib.common.util.log
import com.allever.lib.common.util.loge
import com.allever.lib.common.util.toast
import com.allever.lib.permission.PermissionListener
import com.allever.lib.permission.PermissionManager
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import java.io.File

class BackupRestorePresenter : BasePresenter<BackupRestoreView>() {
    private val BACKUP_FILE_PATH =
        Environment.getExternalStorageDirectory().absolutePath + File.separator + App.context.packageName + File.separator + "backup" + File.separator + "data.json"

    fun backup(task: Runnable) {

        PermissionManager.request(object : PermissionListener {
            override fun onGranted(grantedList: MutableList<String>) {
                kotlin.run {
                    val historyList = DBHelper.getAllHistory()
                    if (historyList.isEmpty()) {
                        toast(R.string.no_backup_data)
                        task.run()
                        return
                    }
                    val backupBean = BackupBean()
                    backupBean.data = historyList
                    val result = Gson().toJson(backupBean)
                    log("backupResult = $result")
                    val success = FileUtil.saveStringToFile(result, BACKUP_FILE_PATH)
                    if (success) {
                        toast(R.string.backup_success)
                    } else {
                        toast(R.string.backup_fail)
                    }
                    task.run()
                }
            }

            override fun onDenied(deniedList: MutableList<String>) {
                toast(R.string.no_wire_store_permission_tips)
                task.run()
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    }

    fun restore(task: Runnable) {
        PermissionManager.request(object : PermissionListener {
            override fun onGranted(grantedList: MutableList<String>) {
                kotlin.run {
                    val data = FileUtil.readFileToString(BACKUP_FILE_PATH)
                    if (data == null || data.isEmpty()) {
                        toast(R.string.no_backup_data)
                        task.run()
                        return
                    }

                    try {
                        val backupBean = JsonHelper.json2Object(data!!, BackupBean::class.java)
                        val historyList = backupBean?.data
                        historyList?.map {
                            log("记录：${it.srcText}")
                            val record = DBHelper.getHistory(it.srcText, it.sl, it.tl)
                            if (record == null) {
                                val history = History()
                                history.srcText = it.srcText
                                history.sl = it.sl
                                history.tl = it.tl
                                history.time = it.time
                                history.liked = it.liked
                                history.result = it.result
                                history.ttsPath = it.ttsPath
                                val saveResult = history.save()
                                if (saveResult) {
                                    log("恢复翻译成功")
                                } else {
                                    loge("恢复翻译失败")
                                }
                            }
                        }
                        EventBus.getDefault().post(RestoreEvent())
                        toast(R.string.restore_success)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        toast(R.string.restore_fail)
                    }
                    task.run()
                }
            }

            override fun onDenied(deniedList: MutableList<String>) {
                toast(R.string.no_read_store_permission_tips)
                task.run()
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun delBackup(task: Runnable) {

        PermissionManager.request(object : PermissionListener {
            override fun onGranted(grantedList: MutableList<String>) {
                kotlin.run {
                    FileUtil.deleteFile(BACKUP_FILE_PATH)
                    task.run()
                    toast(getString(R.string.backup_success))
                }
            }

            override fun onDenied(deniedList: MutableList<String>) {
                toast(R.string.no_wire_store_permission_tips)
                task.run()
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE)

    }
}