package com.allever.lib.recommend

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.allever.lib.common.app.App
import com.allever.lib.common.util.DisplayUtils
import com.allever.lib.common.util.SystemUtils
import com.allever.lib.common.util.Tool
import com.allever.lib.common.util.log
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.runOnUiThread

class RecommendDialog(var context: Activity) : Dialog(context, R.style.CommonCustomDialogStyle) {

    private var mListener: RecommendDialogListener? = null

    private var mList = mutableListOf<Recommend>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_recommend)
        //初始化界面控件
        initView()
    }

    /**
     * 初始化界面控件
     */
    private fun initView() {
        mList.clear()

        val tvMore = findViewById<View>(R.id.tvMore)
        val tvRefuse = findViewById<View>(R.id.tvRefuse)


        val ivA = findViewById<ImageView>(R.id.ivRecommendA)
        val ivB = findViewById<ImageView>(R.id.ivRecommendB)
        val ivC = findViewById<ImageView>(R.id.ivRecommendC)

        val mRandom = java.util.Random()
        val total = RecommendGlobal.recommendData.size
        val index =  if (total <= 2) {
            0
        } else {
            mRandom.nextInt(total - 2)
        }
        log("index = $index")

        if (total >= 3) {
            for (i in index .. index + 2) {
                log("select $i")
                mList.add(RecommendGlobal.recommendData[i])
            }
            ivA.visibility = View.VISIBLE
            ivB.visibility = View.VISIBLE
            ivC.visibility = View.VISIBLE

            Glide.with(context).load(mList[0].iconUrl).into(ivA)
            Glide.with(context).load(mList[1].iconUrl).into(ivB)
            Glide.with(context).load(mList[2].iconUrl).into(ivC)
        } else {
            for (i in 0 until total) {
                log("select $i")
                mList.add(RecommendGlobal.recommendData[i])
            }
            when(total) {
                0 -> {
                    ivA.visibility = View.GONE
                    ivB.visibility = View.GONE
                    ivC.visibility = View.GONE
                }
                1 -> {
                    Glide.with(context).load(mList[0].iconUrl).into(ivA)
                    ivA.visibility = View.VISIBLE
                    ivB.visibility = View.GONE
                    ivC.visibility = View.GONE
                }
                2 -> {
                    Glide.with(context).load(mList[0].iconUrl).into(ivA)
                    Glide.with(context).load(mList[1].iconUrl).into(ivB)
                    ivA.visibility = View.VISIBLE
                    ivB.visibility = View.VISIBLE
                    ivC.visibility = View.GONE
                }
            }
        }


        ivA.setOnClickListener {
            //如果安装了谷歌商店，则打开google商店
            handleIvClick(context, 0)
        }

        ivB.setOnClickListener {
            handleIvClick(context, 1)
        }

        ivC.setOnClickListener {
            handleIvClick(context, 2)
        }

        tvMore.setOnClickListener {
            RecommendActivity.start(context, RecommendGlobal.channel)
            mListener?.onMore(this)
        }

        tvRefuse.setOnClickListener {
            mListener?.onReject(this)
        }
    }

    fun setListener(listener: RecommendDialogListener?) {
        mListener = listener
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mListener?.onBackPress(this)
            return true
        }

        return false
    }

    override fun show() {
        super.show()
        window?.setLayout(DisplayUtils.dip2px(320), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun handleIvClick(activity: Activity, index: Int) {
        val item = mList[index]
        val url = RecommendGlobal.getItemUrl(item)
        GlobalScope.launch {
            SystemUtils.openUrl(activity, url)
            delay(1000)
            App.context.runOnUiThread {
                dismiss()
            }
        }
    }
}
