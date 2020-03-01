package com.allever.app.translation.text.ui

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.allever.app.translation.text.R

import com.allever.app.translation.text.app.BaseActivity
import com.allever.app.translation.text.ui.mvp.presenter.BackupRestorePresenter
import com.allever.app.translation.text.ui.mvp.view.BackupRestoreView




class BackupRestoreActivity : BaseActivity<BackupRestoreView, BackupRestorePresenter>(),
    BackupRestoreView,
    View.OnClickListener {

    private lateinit var mBtnBackup: Button
    private lateinit var mBtnRestore: Button
    private lateinit var mBtnDelBackup: Button




    override fun getContentView(): Any = R.layout.activity_backup_restore

    override fun initView() {
        findViewById<View>(R.id.iv_left).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_label).text = getString(R.string.backup_restore)
        mBtnBackup = findViewById(R.id.btnBackup)
        mBtnBackup.setOnClickListener(this)
        mBtnRestore = findViewById(R.id.btnRestore)
        mBtnRestore.setOnClickListener(this)
        mBtnDelBackup = findViewById(R.id.btnDeleteBackup)
        mBtnDelBackup.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun createPresenter(): BackupRestorePresenter = BackupRestorePresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_left -> {
                finish()
            }
            R.id.btnBackup -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.warm_tips)
                    .setMessage(R.string.backup_tips)
                    .setPositiveButton(
                        R.string.backup
                    ) { dialog, which ->
                        mBtnBackup.isClickable = false
                        mPresenter?.backup(Runnable {
                            mBtnBackup.isClickable = true

                        })
                        dialog.dismiss()
                    }
                    .setNegativeButton(
                        R.string.cancle
                    ) { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
            R.id.btnRestore -> {
                mBtnRestore.isClickable = false
                mPresenter?.restore(Runnable {
                    mBtnRestore.isClickable = true

                })
            }

            R.id.btnDeleteBackup -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.warm_tips)
                    .setMessage(R.string.del_backup_tips)
                    .setPositiveButton(
                        R.string.del_backup
                    ) { dialog, which ->
                        mBtnDelBackup.isClickable = false
                        mPresenter?.delBackup(Runnable {
                            mBtnDelBackup.isClickable = true

                        })
                        dialog.dismiss()
                    }
                    .setNegativeButton(
                        R.string.cancle
                    ) { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

    }





    override fun onDestroy() {
        super.onDestroy()


        setResult(Activity.RESULT_OK)
    }


    companion object {
        val RC_RESULT = 0X01
        fun start(activity: Activity) {
            val intent = Intent(activity, BackupRestoreActivity::class.java)
            activity.startActivityForResult(intent, RC_RESULT)
        }
    }

}