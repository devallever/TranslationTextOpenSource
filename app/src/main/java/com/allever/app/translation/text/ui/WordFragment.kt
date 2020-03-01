package com.allever.app.translation.text.ui

import android.view.View
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.allever.android.recyclerview.widget.MyLinearLayoutManager
import com.allever.android.recyclerview.widget.MyRecyclerView
import com.allever.app.translation.text.R
import com.allever.app.translation.text.app.BaseFragment
import com.allever.app.translation.text.app.Global
import com.allever.app.translation.text.bean.*
import com.allever.app.translation.text.ui.adapter.WordAdapter
import com.allever.app.translation.text.ui.dialog.DialogHelper
import com.allever.app.translation.text.ui.mvp.presenter.WordPresenter
import com.allever.app.translation.text.ui.mvp.view.WordView
import com.allever.lib.common.util.toast
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WordFragment : BaseFragment<WordView, WordPresenter>(), WordView, View.OnClickListener {

    private lateinit var mRvHistory: MyRecyclerView
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

    override fun getContentView(): Int = R.layout.fragment_word

    override fun initView(root: View) {
        EventBus.getDefault().register(this)

        mTvSrcLanguage = root.findViewById(R.id.tvSrcLanguage)
        mTvSrcLanguage.text = Lang.ALL.KEY
        mTvSrcLanguage.setOnClickListener(this)
        mTvTranslateLanguage = root.findViewById(R.id.tvTranslateLanguage)
        mTvTranslateLanguage.text = Lang.ALL.KEY
        mTvTranslateLanguage.setOnClickListener(this)

        mBottomBar = root.findViewById(R.id.rlBottomToolBar)
        mCbSelectAll = root.findViewById(R.id.cbBottomBarCheckAll)
        mCbSelectAll.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.allMode = true
            mAdapter?.allCheck = isChecked
        }
        root.findViewById<View>(R.id.ivBottomBarBack).setOnClickListener(this)
        root.findViewById<View>(R.id.ivBottomBarDelete).setOnClickListener(this)

        mSelectTranslateLangDialog = DialogHelper.createSelectLangDialog(
            activity,
            listener = object : DialogHelper.SelectLangListener {
                override fun onItemSelected(AlertDialog: AlertDialog?, data: SelectLangItem) {
                    AlertDialog?.dismiss()
                    mTvTranslateLanguage.text = data.lang?.KEY ?: Lang.ALL.KEY
                    getLikeHistory()
                    mEditMode = false
                }
            })
        mSelectSrcLangDialog = DialogHelper.createSelectLangDialog(
            activity,
            object : DialogHelper.SelectLangListener {
                override fun onItemSelected(AlertDialog: AlertDialog?, data: SelectLangItem) {
                    AlertDialog?.dismiss()
                    mTvSrcLanguage.text = data.lang?.KEY ?: Lang.ALL.KEY
                    getLikeHistory()
                    mEditMode = false
                }
            })

        mRvHistory = root.findViewById(R.id.rvWords)
        mRvHistory.layoutManager = MyLinearLayoutManager(activity)
        mAdapter = WordAdapter(activity!!, R.layout.item_word, mWordItemList)
        mRvHistory.adapter = mAdapter
        mAdapter?.itemOptionListener = object : WordAdapter.OnItemOptionClick {
            override fun onItemClicked(position: Int) {
                val dialog = DialogHelper.createTranslateResultDialog(
                    activity,
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
//                toast("onItemClicked")
            }

            override fun onRemoveClicked(position: Int) {
//                toast("onRemoveClicked")
                AlertDialog.Builder(activity!!)
                    .setMessage(R.string.remove_tips)
                    .setCancelable(true)
                    .setPositiveButton(R.string.ok) { dialog, which ->
                        mPresenter.removeLiked(mWordItemList[position].history)
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
        getLikeHistory()
    }

    override fun createPresenter(): WordPresenter = WordPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
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
                AlertDialog.Builder(activity!!)
                    .setMessage(R.string.remove_tips)
                    .setCancelable(true)
                    .setPositiveButton(R.string.ok) { dialog, which ->
                        mPresenter.removeLikes(mAdapter?.selectedItem)
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.cancle) { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mEditMode) {
            mEditMode = false
            return true
        }
        return false
    }

    private fun getLikeHistory() {
        val sl = Global.langKeyCodeMap[mTvSrcLanguage.text] ?: ""
        val tl = Global.langKeyCodeMap[mTvTranslateLanguage.text] ?: ""
        mPresenter.getLikedHistory(sl, tl)
        initEditMode()
    }

    private fun initEditMode() {
        mAdapter?.selectedItem?.clear()
        mEditMode = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateRecordEvent(updateRecordEvent: UpdateRecordEvent) {
//        initEditMode()
        getLikeHistory()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLikeUpdate(likeUpdateEvent: LikeUpdateEvent) {
//        initEditMode()
        getLikeHistory()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRemoveLikesEvent(removeLikesEvent: RemoveLikesEvent) {
//        initEditMode()
        getLikeHistory()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRestoreEvent(restoreEvent: RestoreEvent) {
        getLikeHistory()
    }


    override fun updateWordList(data: MutableList<WordItem>) {
        mWordItemList.clear()
        mWordItemList.addAll(data)
        mAdapter?.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): WordFragment = WordFragment()
    }
}