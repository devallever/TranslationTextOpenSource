package com.allever.app.translation.text.ui


import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.allever.android.widget.MySwitchCompat
import com.allever.app.translation.text.R

import com.allever.app.translation.text.app.BaseFragment
import com.allever.app.translation.text.bean.DefaultTranslateLangChangedEvent
import com.allever.app.translation.text.bean.Lang
import com.allever.app.translation.text.bean.SelectLangItem
import com.allever.app.translation.text.function.SettingHelper
import com.allever.app.translation.text.ui.dialog.DialogHelper
import com.allever.app.translation.text.ui.mvp.presenter.SettingPresenter
import com.allever.app.translation.text.ui.mvp.view.SettingView
import com.allever.app.translation.text.util.CommonHelper



import com.allever.lib.common.app.App
import com.allever.lib.common.util.*
import com.allever.lib.permission.PermissionUtil
import com.allever.lib.umeng.UMeng
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SettingFragment : BaseFragment<SettingView, SettingPresenter>(), SettingView,
    View.OnClickListener {

    private lateinit var mBannerContainer: ViewGroup


    private lateinit var mTvDefaultTranslateLang: TextView
    private var mLangDialog: AlertDialog? = null

    override fun getContentView(): Int = R.layout.fragment_setting

    override fun initView(root: View) {
        EventBus.getDefault().register(this)
        root.findViewById<View>(R.id.setting_tv_share).setOnClickListener(this)
        root.findViewById<TextView>(R.id.setting_tv_feedback).setOnClickListener(this)
        root.findViewById<TextView>(R.id.setting_tv_about).setOnClickListener(this)
        root.findViewById<TextView>(R.id.setting_tv_permission).setOnClickListener(this)
        root.findViewById<TextView>(R.id.setting_tv_guide).setOnClickListener(this)
        root.findViewById<TextView>(R.id.setting_tv_support).setOnClickListener(this)
        root.findViewById<View>(R.id.itemDefaultTranslateLang).setOnClickListener(this)
        root.findViewById<View>(R.id.itemBackupRestore).setOnClickListener(this)

        val switchAutoPlayAudio = root.findViewById<MySwitchCompat>(R.id.switchAutoPlayAudio)
        switchAutoPlayAudio.isChecked = SettingHelper.getAutoPlayAudio()
        switchAutoPlayAudio.setOnCheckedChangeListener { buttonView, isChecked ->
            SettingHelper.setAutoPlayAudioSwitch(isChecked)
        }

        val switchCopyToClipBoard = root.findViewById<MySwitchCompat>(R.id.switchCopyToClipboard)
        switchCopyToClipBoard.isChecked = SettingHelper.getAutoPlayAudio()
        switchCopyToClipBoard.setOnCheckedChangeListener { buttonView, isChecked ->
            SettingHelper.setCopyClipBoardSwitch(isChecked)
        }

        val switchTranslate = root.findViewById<MySwitchCompat>(R.id.switchAutoTranslate)
        switchTranslate.isChecked = SettingHelper.getAutoTranslate()
        switchTranslate.setOnCheckedChangeListener { buttonView, isChecked ->
            SettingHelper.setAutoTranslateSwitch(isChecked)
            if (CommonHelper.isAboveAndrodQ()) {
                toast("您的Android版本不支持改功能, 因为Android10已禁用访问剪贴板功能.")
            }
        }

        val switchForegroundService =
            root.findViewById<MySwitchCompat>(R.id.switchForegroundService)
        switchForegroundService.isChecked = SettingHelper.getForegroundServiceSwitch()
        switchForegroundService.setOnCheckedChangeListener { buttonView, isChecked ->
            SettingHelper.setForegroundServiceSwitch(isChecked)
        }

        mTvDefaultTranslateLang = root.findViewById(R.id.tvDefaultTranslateLang)
        mTvDefaultTranslateLang.text = SettingHelper.getDefaultTranslateLangKey()
        mLangDialog = DialogHelper.createSelectLangDialog(
            activity,
            1,
            object : DialogHelper.SelectLangListener {
                override fun onItemSelected(alertDialog: AlertDialog?, data: SelectLangItem) {
                    alertDialog?.dismiss()
                    mTvDefaultTranslateLang.text = data.lang?.KEY
                    SettingHelper.setDefaultTranslateLang(data.lang?.KEY ?: Lang.CHINESE.KEY)
                }
            })

        mBannerContainer = root.findViewById(R.id.banner_container)
    }

    override fun initData() {

    }

    override fun createPresenter(): SettingPresenter = SettingPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.setting_tv_guide -> {
                ActivityCollector.startActivity(activity!!, GuideActivity::class.java)
            }
            R.id.setting_tv_permission -> {
                PermissionUtil.GoToSetting(activity)
            }
            R.id.setting_tv_share -> {
                val msg = getString(
                    R.string.share_content,
                    getString(R.string.app_name),
                    App.context.packageName
                )
                ShareHelper.shareText(this, msg)
            }
            R.id.setting_tv_feedback -> {
                FeedbackHelper.feedback(activity)
            }
            R.id.setting_tv_about -> {
                AboutActivity.start(activity!!)
            }
            R.id.setting_tv_support -> {
                Tool.openInGooglePlay(activity!!, App.context.packageName)
            }
            R.id.itemDefaultTranslateLang -> {
                mLangDialog?.show()
            }
            R.id.itemBackupRestore -> {
                BackupRestoreActivity.start(activity!!)
            }
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDefaultTranslateLangChanged(defaultTranslateLangChangedEvent: DefaultTranslateLangChangedEvent) {
        mTvDefaultTranslateLang.text = SettingHelper.getDefaultTranslateLangKey()
    }

    companion object {
        fun newInstance(): SettingFragment = SettingFragment()
    }

}