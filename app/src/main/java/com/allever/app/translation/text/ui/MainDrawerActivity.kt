package com.allever.app.translation.text.ui

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.allever.app.translation.text.R

import com.allever.app.translation.text.app.BaseActivity
import com.allever.app.translation.text.function.SettingHelper
import com.allever.app.translation.text.function.TranslationService
import com.allever.app.translation.text.ui.mvp.presenter.MainPresenter
import com.allever.app.translation.text.ui.mvp.view.MainView



import com.allever.lib.comment.CommentHelper
import com.allever.lib.comment.CommentListener
import com.allever.lib.common.app.App
import com.allever.lib.common.util.ActivityCollector
import com.allever.lib.common.util.Tool
import com.allever.lib.common.util.toast
import com.allever.lib.recommend.RecommendActivity
import com.allever.lib.recommend.RecommendDialogHelper
import com.allever.lib.recommend.RecommendDialogListener
import com.allever.lib.recommend.RecommendGlobal
import com.allever.lib.ui.widget.ShakeHelper
import com.allever.lib.umeng.UMeng
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_drawer_main.*
import kotlinx.android.synthetic.main.include_top_bar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainDrawerActivity : BaseActivity<MainView, MainPresenter>(), MainView,
    NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private lateinit var mShakeObjectAnimator: ObjectAnimator




    override fun getContentView(): Any = R.layout.activity_drawer_main

    override fun initView() {
        iv_left.setImageResource(R.drawable.ic_menu)
        iv_left.setOnClickListener(this)
        tv_label.text = getString(R.string.app_name)
        iv_right.setImageResource(R.drawable.recommend_ic_gift)
        iv_right.clearColorFilter()
        iv_right.visibility = View.VISIBLE
        iv_right.setOnClickListener(this)
        mShakeObjectAnimator = ShakeHelper.createShakeAnimator(iv_right, true)
        mShakeObjectAnimator.start()
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun initData() {
        mPresenter?.requestPermission(this)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, TranslationFragment.newInstance())
        transaction.commit()

        //加载插屏

    }

    override fun createPresenter(): MainPresenter = MainPresenter()

    override fun updateResult(result: String) {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_left -> {
                drawerLayout.openDrawer(Gravity.LEFT)
            }
            R.id.iv_right -> {
                RecommendActivity.start(this, UMeng.getChannel())
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        item.isCheckable = true
        item.isChecked = true
        drawerLayout.postDelayed({
            when (item.itemId) {
                R.id.nav_history -> {
                    ActivityCollector.startActivity(this, HistoryActivity::class.java)
                }
                R.id.nav_word -> {
                    ActivityCollector.startActivity(this, WordActivity::class.java)
                }
                R.id.nav_backup -> {
                    ActivityCollector.startActivity(this, BackupRestoreActivity::class.java)
                }
                R.id.nav_guide -> {
                    ActivityCollector.startActivity(this, GuideActivity::class.java)
                }
                R.id.nav_setting -> {
                    ActivityCollector.startActivity(this, SettingActivity::class.java)
                }
                R.id.nav_about -> {
                    ActivityCollector.startActivity(this, AboutActivity::class.java)
                }
                R.id.nav_support -> {
                    Tool.openInGooglePlay(this, App.context.packageName)
                }
            }
            drawerLayout.closeDrawers()
            item.isChecked = false
        }, 300)

        return true
    }

    override fun onDestroy() {
        mShakeObjectAnimator.cancel()
        if (SettingHelper.getForegroundServiceSwitch()) {
            //启动一个前台服务
            TranslationService.start(this)
        } else {
            TranslationService.stop(this)
        }


        super.onDestroy()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawers()
            return
        }

        if (UMeng.getChannel() == "google") {
            //谷歌渠道，首次评分，其余推荐
            if (mIsShowComment) {
                if (RecommendGlobal.recommendData.isEmpty()) {
                    showComment()
                } else {
                    showRecommendDialog()
                }
            } else {
                showComment()
            }
        } else {
            //其他渠道推荐
            if (RecommendGlobal.recommendData.isEmpty()) {
                checkExit()
            } else {
                showRecommendDialog()
            }
        }
    }

    private fun showRecommendDialog() {
        val dialog = RecommendDialogHelper.createRecommendDialog(this, object :
            RecommendDialogListener {
            override fun onMore(dialog: Dialog?) {
                dialog?.dismiss()
            }

            override fun onReject(dialog: Dialog?) {
                dialog?.dismiss()
                GlobalScope.launch {
                    delay(200)
                    finish()
                }
            }

            override fun onBackPress(dialog: Dialog?) {
                dialog?.dismiss()
                GlobalScope.launch {
                    delay(200)
                    finish()
                }
            }
        })

        RecommendDialogHelper.show(this, dialog)
    }

    private var mIsShowComment = false
    private fun showComment() {
        val dialog = CommentHelper.createCommentDialog(this, object : CommentListener {
            override fun onComment(dialog: Dialog?) {
                dialog?.dismiss()
            }

            override fun onReject(dialog: Dialog?) {
                dialog?.dismiss()
                GlobalScope.launch {
                    delay(200)
                    finish()
                }
            }

            override fun onBackPress(dialog: Dialog?) {
                dialog?.dismiss()
                GlobalScope.launch {
                    delay(200)
                    finish()
                }
            }
        })

        CommentHelper.show(this, dialog)
        mIsShowComment = true
    }
}