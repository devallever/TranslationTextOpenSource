package com.allever.lib.recommend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.TextView
import com.allever.android.recyclerview.widget.MyLinearLayoutManager
import com.allever.android.recyclerview.widget.MyRecyclerView
import com.allever.lib.common.app.BaseActivity
import com.allever.lib.common.ui.widget.recycler.BaseViewHolder
import com.allever.lib.common.ui.widget.recycler.ItemListener
import com.allever.lib.common.util.*
import com.google.gson.Gson
import java.io.File

class RecommendActivity : BaseActivity(), View.OnClickListener {

    private var mRecommendData = mutableListOf<Recommend>()
    private lateinit var mRvRecommendList: MyRecyclerView
    private var mAdapter: RecommendAdapter? = null
    private var mUmengChannel = ""

    private var mRecommendListener = object : RecommendListener {
        override fun onSuccess(data: MutableList<Recommend>) {
            mRecommendData.clear()
            mRecommendData.addAll(data)
            mAdapter?.notifyDataSetChanged()
        }

        override fun onFail() {
            toast("No Recommend")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        mUmengChannel = intent?.getStringExtra(EXTRA_CHANNEL) ?: ""

        findViewById<View>(R.id.iv_back).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_label).text = getString(R.string.recommend)

        mRecommendData.addAll(RecommendGlobal.recommendData)
        mAdapter = RecommendAdapter(this, R.layout.item_recommend, mRecommendData)
        mRvRecommendList = findViewById(R.id.rvRecommendList)
        mRvRecommendList.layoutManager = MyLinearLayoutManager(this)
        mRvRecommendList.adapter = mAdapter
        mAdapter?.setItemListener(object : ItemListener {
            override fun onItemClick(position: Int, holder: BaseViewHolder) {
                //如果安装了谷歌商店，则打开google商店
                val item = mRecommendData[position]
                val url = RecommendGlobal.getItemUrl(item)
                SystemUtils.openUrl(this@RecommendActivity, url)
            }
        })

        if (RecommendGlobal.recommendData.isEmpty()) {
            RecommendGlobal.getRecommendData(mUmengChannel, mRecommendListener)
        }

//        if (BuildConfig.DEBUG) {
//            getLocalRecommendData()
//        } else {
//            if (RecommendGlobal.recommendData.isEmpty()) {
//                getRecommendData()
//            }
//        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }

    private fun getLocalRecommendData() {
        val path = Environment.getExternalStorageDirectory().absolutePath + File.separator + "recommend.json"
        val recommendData = FileUtils.readTextFile(path)
        try {
            val gson = Gson()
            val recommend = gson.fromJson(recommendData, RecommendBean::class.java)
            RecommendGlobal.handleRecommendData(recommend, mUmengChannel, mRecommendListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val EXTRA_CHANNEL = "EXTRA_CHANNEL"
        fun start(context: Context, channel: String) {
            val intent = Intent(context, RecommendActivity::class.java)
            intent.putExtra(EXTRA_CHANNEL, channel)
            context.startActivity(intent)
        }
    }
}