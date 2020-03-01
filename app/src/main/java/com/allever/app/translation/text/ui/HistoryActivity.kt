package com.allever.app.translation.text.ui

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.allever.android.recyclerview.widget.MyLinearLayoutManager
import com.allever.android.recyclerview.widget.MyRecyclerView
import com.allever.app.translation.text.R

import com.allever.app.translation.text.app.BaseActivity
import com.allever.app.translation.text.app.Global
import com.allever.app.translation.text.bean.*
import com.allever.app.translation.text.ui.adapter.WordAdapter
import com.allever.app.translation.text.ui.dialog.DialogHelper
import com.allever.app.translation.text.ui.mvp.presenter.HistoryPresenter
import com.allever.app.translation.text.ui.mvp.view.HistoryView



import com.allever.lib.common.util.ActivityCollector
import kotlinx.android.synthetic.main.activity_history.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

class HistoryActivity : BaseActivity<HistoryView, HistoryPresenter>(), HistoryView,
    View.OnClickListener {


    private lateinit var mRvWord: MyRecyclerView
    private var mAdapter: WordAdapter? = null
    private val mWordItemList = mutableListOf<WordItem>()

    private var mSelectTranslateLangDialog: AlertDialog? = null
    private var mSelectSrcLangDialog: AlertDialog? = null

    private lateinit var mTvSrcLanguage: TextView
    private lateinit var mTvTranslateLanguage: TextView

    private lateinit var mBottomBar: ViewGroup
    private lateinit var mCbSelectAll: CheckBox

    private var mEditMode = false
        set(value) {
            field = value
            mBottomBar.visibility = if (value) View.VISIBLE else View.GONE
            mAdapter?.editMode = value
        }

    override fun getContentView(): Any = R.layout.activity_history

    override fun initView() {
        EventBus.getDefault().register(this)
        findViewById<View>(R.id.iv_left).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_label).text = getString(R.string.title_history)
        val ivRight = findViewById<ImageView>(R.id.iv_right)
        ivRight.setOnClickListener(this)
        ivRight.visibility = View.VISIBLE
        ivRight.setImageResource(R.drawable.ic_backup)

        mTvSrcLanguage = findViewById(R.id.tvSrcLanguage)
        mTvSrcLanguage.text = Lang.ALL.KEY
        mTvSrcLanguage.setOnClickListener(this)
        mTvTranslateLanguage = findViewById(R.id.tvTranslateLanguage)
        mTvTranslateLanguage.text = Lang.ALL.KEY
        mTvTranslateLanguage.setOnClickListener(this)

        mBottomBar = findViewById(R.id.rlBottomToolBar)
        mCbSelectAll = findViewById(R.id.cbBottomBarCheckAll)
        mCbSelectAll.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.allMode = true
            mAdapter?.allCheck = isChecked
        }
        findViewById<View>(R.id.ivBottomBarBack).setOnClickListener(this)
        findViewById<View>(R.id.ivBottomBarDelete).setOnClickListener(this)

        mSelectTranslateLangDialog = DialogHelper.createSelectLangDialog(
            this,
            listener = object : DialogHelper.SelectLangListener {
                override fun onItemSelected(AlertDialog: AlertDialog?, data: SelectLangItem) {
                    AlertDialog?.dismiss()
                    mTvTranslateLanguage.text = data.lang?.KEY ?: Lang.ALL.KEY
                    getHistory()
                    mEditMode = false
                }
            })
        mSelectSrcLangDialog = DialogHelper.createSelectLangDialog(
            this,
            object : DialogHelper.SelectLangListener {
                override fun onItemSelected(AlertDialog: AlertDialog?, data: SelectLangItem) {
                    AlertDialog?.dismiss()
                    mTvSrcLanguage.text = data.lang?.KEY ?: Lang.ALL.KEY
                    getHistory()
                    mEditMode = false
                }
            })

        mRvWord = findViewById(R.id.rvHistory)
        mRvWord.layoutManager = MyLinearLayoutManager(this)
        mAdapter = WordAdapter(this, R.layout.item_word, mWordItemList)
        mRvWord.adapter = mAdapter
        mAdapter?.itemOptionListener = object : WordAdapter.OnItemOptionClick {
            override fun onItemClicked(position: Int) {
                val dialog = DialogHelper.createTranslateResultDialog(
                    this@HistoryActivity,
                    mWordItemList[position].history
                )
                dialog?.show()
            }

            override fun onLongClick(position: Int) {
                if (!mEditMode) {
                    mEditMode = true
                }
            }

            override fun onLikeClicked(position: Int) {
                mPresenter.liked(mWordItemList[position].history)
            }

            override fun onRemoveClicked(position: Int) {
//                toast("onRemoveClicked")
                AlertDialog.Builder(this@HistoryActivity)
                    .setMessage(R.string.remove_tips)
                    .setCancelable(true)
                    .setPositiveButton(R.string.ok) { dialog, which ->
                        mPresenter.remove(mWordItemList[position].history)
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.cancle) { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }

        }
    }

    override fun initData() {

        getHistory()
    }

    override fun createPresenter(): HistoryPresenter = HistoryPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_left -> {
                onBackPressed()
            }
            R.id.iv_right -> {
                ActivityCollector.startActivity(this, BackupRestoreActivity::class.java)
            }
            R.id.tvTranslateLanguage -> {
                mSelectTranslateLangDialog?.show()
            }

            R.id.tvSrcLanguage -> {
                mSelectSrcLangDialog?.show()
            }
            R.id.ivBottomBarBack -> {
                mEditMode = false
            }
            R.id.ivBottomBarDelete -> {
                if (mAdapter?.selectedItem?.isEmpty() == true) {
                    toast(R.string.un_slelectd)
                    return
                }
                AlertDialog.Builder(this)
                    .setMessage(R.string.remove_tips)
                    .setCancelable(true)
                    .setPositiveButton(R.string.ok) { dialog, which ->
                        mPresenter.removeHistories(mAdapter?.selectedItem)
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.cancle) { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    override fun onBackPressed() {
        if (mEditMode) {
            mEditMode = false
            return
        }
        return super.onBackPressed()
    }

    private fun getHistory() {
        val sl = Global.langKeyCodeMap[mTvSrcLanguage.text] ?: ""
        val tl = Global.langKeyCodeMap[mTvTranslateLanguage.text] ?: ""
        mPresenter.getHistory(sl, tl)
        initEditMode()
    }

    private fun initEditMode() {
        mAdapter?.selectedItem?.clear()
        mEditMode = false
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        setResult(Activity.RESULT_OK)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLikeUpdate(likeUpdateEvent: LikeUpdateEvent) {
//        initEditMode()
        getHistory()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRemoveLikesEvent(removeLikesEvent: RemoveLikesEvent) {
//        initEditMode()
        getHistory()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateRecordEvent(recordEvent: UpdateRecordEvent) {
//        initEditMode()
        getHistory()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRestoreEvent(restoreEvent: RestoreEvent) {
        getHistory()
    }


    override fun updateWordList(data: MutableList<WordItem>) {
        mWordItemList.clear()
        mWordItemList.addAll(data)
        mAdapter?.notifyDataSetChanged()
    }





    companion object {
        val RC_RESULT = 0X01
        fun start(activity: Activity) {
            val intent = Intent(activity, HistoryActivity::class.java)
            activity.startActivityForResult(intent, RC_RESULT)
        }
    }
}