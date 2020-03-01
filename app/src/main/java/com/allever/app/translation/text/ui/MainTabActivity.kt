package com.allever.app.translation.text.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.PorterDuff
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.allever.android.app.MyFragment
import com.allever.android.view.MyViewPager
import com.allever.app.translation.text.R

import com.allever.app.translation.text.app.BaseActivity
import com.allever.app.translation.text.bean.RecognizedEvent
import com.allever.app.translation.text.function.SettingHelper
import com.allever.app.translation.text.function.TranslationService
import com.allever.app.translation.text.ui.mvp.presenter.MainPresenter
import com.allever.app.translation.text.ui.mvp.view.MainView



import com.allever.lib.comment.CommentHelper
import com.allever.lib.comment.CommentListener
import com.allever.lib.common.app.BaseFragment
import com.allever.lib.common.ui.adapter.ViewPagerAdapter
import com.allever.lib.common.ui.widget.tab.TabLayout
import com.allever.lib.common.util.DisplayUtils
import com.allever.lib.common.util.SystemUtils
import com.allever.lib.recommend.*
import com.allever.lib.umeng.UMeng
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus


class MainTabActivity : BaseActivity<MainView, MainPresenter>(), MainView,
    TabLayout.OnTabSelectedListener, View.OnClickListener {
    private lateinit var mVp: MyViewPager
    private lateinit var mViewPagerAdapter: ViewPagerAdapter
    private lateinit var mTab: TabLayout
    private lateinit var mTvTitle: TextView
    private lateinit var mIvRight: ImageView
    private var mainTabHighlight = 0
    private var mainTabUnSelectColor = 0

    private var mFragmentList = mutableListOf<MyFragment>()

//    private var mInsertAd = AdHelper.createAd(ADType.INSERT)




    private var mPagePosition = 0

    override fun getContentView(): Any = R.layout.activity_main_tab

    override fun initView() {
        mIvRight = findViewById(R.id.iv_right)
        mIvRight.setImageResource(R.drawable.recommend_ic_gift)
        mIvRight.setOnClickListener(this)
        mTab = findViewById(R.id.tab_layout)
        mVp = findViewById(R.id.id_main_vp)
        mTvTitle = findViewById(R.id.tv_label)

        mainTabHighlight = resources.getColor(R.color.main_tab_highlight)
        mainTabUnSelectColor = resources.getColor(R.color.main_tab_unselect_color)

        updateIvRight(0)
        initViewPagerData()
        initViewPager()
        initTab()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_right -> {
                when (mPagePosition) {
                    0 -> {
                        RecommendActivity.start(this, UMeng.getChannel())
                    }
                    1 -> {
                        HistoryActivity.start(this)
                    }
                    2 -> {
                        RecommendActivity.start(this, UMeng.getChannel())
                    }
                }
            }
        }
    }

    override fun initData() {
//        AdChainHelper.init(this, AdConstant.adData)
        mPresenter?.requestPermission(this)
        //加载插屏

    }

    override fun createPresenter(): MainPresenter = MainPresenter()

    private fun initViewPagerData() {
        mFragmentList.add(TranslationFragment())
        mFragmentList.add(WordFragment())
        mFragmentList.add(SettingFragment())
        mViewPagerAdapter = ViewPagerAdapter(supportFragmentManager, mFragmentList)
    }

    private fun initViewPager() {
        mVp.offscreenPageLimit = 3
        mVp.adapter = mViewPagerAdapter

        mVp.addOnPageChangeListener(object : MyViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mPagePosition = position
                updateIvRight(position)
                when (position) {
                    0 -> {
                        mTvTitle.text = getString(R.string.app_name)
                    }
                    1 -> {
                        mTvTitle.text = getString(R.string.title_words)
                        EventBus.getDefault().post(RecognizedEvent(false))
                    }
                    2 -> {
                        mTvTitle.text = getString(R.string.setting)
                        EventBus.getDefault().post(RecognizedEvent(false))
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun updateIvRight(position: Int) {
        val channel = SystemUtils.getManifestDataByKey(this, "UMENG_CHANNEL")
        when (position) {
            0, 2 -> {
                mIvRight.setImageResource(R.drawable.recommend_ic_gift)
//                if (channel == "google" || channel == "xiaomi") {
//                    setVisibility(mIvRight, false)
//                } else {
//                    setVisibility(mIvRight, true)
//                }
            }
            1 -> {
                mIvRight.setImageResource(R.drawable.ic_history)
//                setVisibility(mIvRight, true)
            }
        }
    }

    private fun initTab() {
        //tab
        mVp.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTab))
        mTab.setOnTabSelectedListener(this)

        val tabCount = TabModel.tabCount
        for (i in 0 until tabCount) {
            val tabModel = TabModel.getTab(i)
            val labelId = tabModel.labelResId
            val tab = mTab.newTab()
                .setTag(tabModel)
                .setCustomView(getTabView(i))
                .setContentDescription(labelId)
            val drawable = tabModel.drawable
            if (drawable != null) {
                tab.icon = drawable
            } else {
                tab.setIcon(tabModel.iconResId)
            }

            tab.setText(labelId)
            val imageView = tab.customView?.findViewById<ImageView>(R.id.icon)
            imageView?.setColorFilter(mainTabUnSelectColor, PorterDuff.Mode.SRC_IN)
            //解决首次tab文字颜色异常
            val textView = tab.customView?.findViewById<TextView>(R.id.text1)
            textView?.setTextColor(mainTabUnSelectColor)
            mTab.addTab(tab)
        }

        mTab.setSelectedTabIndicatorWidth(DisplayUtils.dip2px(0))
        mTab.setSelectedTabIndicatorHeight(DisplayUtils.dip2px(0))
        mTab.setSelectedTabIndicatorColor(mainTabHighlight)
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        mVp.currentItem = tab.position

        TabModel.selectedTab = (tab.tag as TabModel.Tab)
        for (i in 0 until mTab.tabCount) {
            val aTab = mTab.getTabAt(i)
            if (aTab != null) {
                val imageView = aTab.customView?.findViewById<ImageView>(R.id.icon)
                val textView = aTab.customView?.findViewById<TextView>(R.id.text1)
                if (aTab === tab) {
                    imageView?.setColorFilter(mainTabHighlight, PorterDuff.Mode.SRC_IN)
                    textView?.setTextColor(mainTabHighlight)
                } else {
                    imageView?.setColorFilter(mainTabUnSelectColor, PorterDuff.Mode.SRC_IN)
                    textView?.setTextColor(mainTabUnSelectColor)
                }
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {}
    override fun onTabReselected(tab: TabLayout.Tab) {}

    private fun getTabView(position: Int): View {
        val view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_tab, null)
        val imageView = view.findViewById<ImageView>(R.id.icon)
        val textView = view.findViewById<TextView>(R.id.text1)
        val tab = TabModel.getTab(position)
        textView.setText(tab.labelResId)
        imageView.setImageResource(tab.iconResId)
        return view
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val currentFragment = mFragmentList[mVp.currentItem] as BaseFragment
        if (currentFragment.onKeyDown(keyCode, event)) {
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun updateResult(result: String) {

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().post(RecognizedEvent(false))
    }

    override fun onDestroy() {
        if (SettingHelper.getForegroundServiceSwitch()) {
            //启动一个前台服务
            TranslationService.start(this)
        } else {
            TranslationService.stop(this)
        }
//


        super.onDestroy()
    }

    override fun onBackPressed() {
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
        val dialog =
            RecommendDialogHelper.createRecommendDialog(this, object : RecommendDialogListener {
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