package com.allever.lib.recommend

import android.app.Activity
import android.app.Dialog
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.allever.lib.common.app.App
import com.allever.lib.common.util.DisplayUtils
import com.allever.lib.common.util.SystemUtils
import com.allever.lib.common.util.log
import com.allever.lib.recommend.RecommendGlobal.getItemUrl
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.runOnUiThread

object RecommendDialogHelper {

    private var mAlertDialog: AlertDialog? = null

    private var mList = mutableListOf<Recommend>()

    fun createRecommendDialog(activity: Activity?, listener: RecommendDialogListener?): Dialog? {
        activity?: return null
        val dialog = RecommendDialog(activity)
        dialog.setListener(listener)
        return dialog
    }

    fun create(activity: Activity?, listener: RecommendDialogListener?) {
        activity ?: return

        mList.clear()

        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_recommend, null)
//        val llStar = view.findViewById<LinearLayout>(R.id.llStar)
        val tvMore = view.findViewById<View>(R.id.tvMore)
        val tvRefuse = view.findViewById<View>(R.id.tvRefuse)


        val ivA = view.findViewById<ImageView>(R.id.ivRecommendA)
        val ivB = view.findViewById<ImageView>(R.id.ivRecommendB)
        val ivC = view.findViewById<ImageView>(R.id.ivRecommendC)

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

            Glide.with(activity).load(mList[0].iconUrl).into(ivA)
            Glide.with(activity).load(mList[1].iconUrl).into(ivB)
            Glide.with(activity).load(mList[2].iconUrl).into(ivC)
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
                    Glide.with(activity).load(mList[0].iconUrl).into(ivA)
                    ivA.visibility = View.VISIBLE
                    ivB.visibility = View.GONE
                    ivC.visibility = View.GONE
                }
                2 -> {
                    Glide.with(activity).load(mList[0].iconUrl).into(ivA)
                    Glide.with(activity).load(mList[1].iconUrl).into(ivB)
                    ivA.visibility = View.VISIBLE
                    ivB.visibility = View.VISIBLE
                    ivC.visibility = View.GONE
                }
            }
        }


        ivA.setOnClickListener {
            //如果安装了谷歌商店，则打开google商店
            handleIvClick(activity, 0)
        }

        ivB.setOnClickListener {
            handleIvClick(activity, 1)
        }

        ivC.setOnClickListener {
            handleIvClick(activity, 2)
        }

        tvMore.setOnClickListener {
            RecommendActivity.start(activity, RecommendGlobal.channel)
            listener?.onMore(mAlertDialog)
        }

        tvRefuse.setOnClickListener {
            listener?.onReject(mAlertDialog)
        }

        mAlertDialog = AlertDialog.Builder(activity)
            .setView(view)
            .create()

        mAlertDialog?.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                listener?.onBackPress(mAlertDialog)
                return@setOnKeyListener true
            }

            false
        }

        mAlertDialog?.window?.setLayout(
            DisplayUtils.dip2px(280),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        show(activity, mAlertDialog)
    }

    fun show(activity: Activity?, dialog: Dialog?) {
        if (activity?.isFinishing == false) {
            dialog?.show()
            dialog?.window?.setLayout(DisplayUtils.dip2px(280), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    private fun handleIvClick(activity: Activity, index: Int) {
        val item = mList[index]
        val url = getItemUrl(item)
        GlobalScope.launch {
            SystemUtils.openUrl(activity, url)
            delay(1000)
            App.context.runOnUiThread {
                mAlertDialog?.dismiss()
            }
        }
    }


}