package com.allever.app.translation.text.ui.dialog

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.allever.android.recyclerview.widget.MyLinearLayoutManager
import com.allever.android.recyclerview.widget.MyRecyclerView
import com.allever.app.translation.text.R
import com.allever.app.translation.text.app.Global
import com.allever.app.translation.text.bean.Lang
import com.allever.app.translation.text.bean.SelectLangItem
import com.allever.app.translation.text.bean.TranslationBean
import com.allever.app.translation.text.function.TranslationHelper
import com.allever.app.translation.text.function.db.History
import com.allever.app.translation.text.ui.adapter.SelectLangAdapter
import com.allever.app.translation.text.util.ClipboardHelper
import com.allever.app.translation.text.util.JsonHelper
import com.allever.lib.common.ui.widget.recycler.BaseViewHolder
import com.allever.lib.common.ui.widget.recycler.ItemListener
import org.jetbrains.anko.imageResource

object DialogHelper {

    fun createTranslateResultDialog(activity: Activity?, history: History?): AlertDialog? {
        activity ?: return null
        history ?: return null

        //解析数据
        val translationBean =
            JsonHelper.json2Object(history.result, TranslationBean::class.java)

        val root =
            LayoutInflater.from(activity).inflate(R.layout.dialog_translate_result, null, false)

        lateinit var mTranslateResultContainer: View

        lateinit var mTvSoundSrcLanguage: TextView
        lateinit var mTvSrcText: TextView
        lateinit var mTvSrcSymbol: TextView
        lateinit var mIvSrcSound: ImageView
        lateinit var mIvLiked: ImageView
        lateinit var mIvCopySrcText: ImageView


        lateinit var mTvSoundTranslateLanguage: TextView
        lateinit var mTvTranslateText: TextView
        lateinit var mTvTranslateSymbol: TextView
        lateinit var mIvTranslateSound: ImageView
        lateinit var mIvCopyTranslateText: ImageView

        lateinit var mTvResult: TextView

        mTranslateResultContainer = root.findViewById(R.id.llResultContainer)

        mTvSoundSrcLanguage = root.findViewById(R.id.tvSoundSrcLanguage)
        mTvSrcText = root.findViewById(R.id.tvSrcText)
        mTvSrcSymbol = root.findViewById(R.id.tvSrcSymbol)
        mIvSrcSound = root.findViewById(R.id.ivSoundSrc)
        mIvSrcSound.setOnClickListener {
            if (translationBean != null) {
                val srcLang = TranslationHelper.getSrcLang(translationBean)
                TranslationHelper.playTTS(
                    mTvSrcText.text.toString(),
                    Global.langKeyCodeMap[srcLang] ?: Lang.CHINESE.CODE
                )
            }
        }
        mIvLiked = root.findViewById(R.id.ivLiked)
        mIvLiked.setOnClickListener {
            val newHistory = TranslationHelper.liked(history)
            if (newHistory?.liked == 1) {
                mIvLiked.imageResource = R.drawable.ic_star_full
            } else {
                mIvLiked.imageResource = R.drawable.ic_star_empty
            }
        }
        mIvCopySrcText = root.findViewById(R.id.ivCopySrcText)
        mIvCopySrcText.setOnClickListener {
            ClipboardHelper.copy(mTvSrcText.text.toString())
        }

        mTvSoundTranslateLanguage = root.findViewById(R.id.tvSoundTranslateLanguage)
        mTvTranslateText = root.findViewById(R.id.tvTranslateText)
        mTvTranslateSymbol = root.findViewById(R.id.tvTranslateSymbol)
        mIvTranslateSound = root.findViewById(R.id.ivSoundTranslate)
        mIvTranslateSound.setOnClickListener {
            TranslationHelper.playTTS(mTvTranslateText.text.toString(), history.tl)
        }
        mIvCopyTranslateText = root.findViewById(R.id.ivCopyTranslateText)
        mIvCopyTranslateText.setOnClickListener {
            ClipboardHelper.copy(mTvTranslateText.text.toString())
        }
        root.findViewById<View>(R.id.ivShareTranslateSound).setOnClickListener {
            val content = mTvTranslateText.text.toString()
            val tl = Global.langKeyCodeMap[mTvSoundTranslateLanguage.text] ?: Lang.CHINESE.CODE
            TranslationHelper.shareAudio(activity, content, tl)
        }

        mTvResult = root.findViewById(R.id.tvResult)

        if (translationBean != null) {
            val srcLang = TranslationHelper.getSrcLang(translationBean)
            val srcText = TranslationHelper.getSrcText(translationBean)
            val srcSymbol = TranslationHelper.getSrcSymbol(translationBean)
            val translateText = TranslationHelper.getTranslateText(translationBean)
            val translateSymbol = TranslationHelper.getTranslateSymbol(translationBean)
            val dictText = TranslationHelper.getDictText(translationBean)
            mTvSoundSrcLanguage.text = srcLang
            if (history.liked == 1) {
                mIvLiked.setImageResource(R.drawable.ic_star_full)
            } else {
                mIvLiked.setImageResource(R.drawable.ic_star_empty)
            }
            mTvSrcText.text = srcText
            mTvSrcSymbol.text = srcSymbol
            mTvSoundTranslateLanguage.text = Global.langCodeKeyMap[history.tl]
            mTvTranslateText.text = translateText
            mTvTranslateSymbol.text = translateSymbol
            mTvResult.text = dictText

        } else {
            mIvSrcSound.visibility = View.GONE
            mIvTranslateSound.visibility = View.GONE
            mIvLiked.visibility = View.GONE
            mIvSrcSound.visibility = View.GONE
            mIvCopySrcText.visibility = View.GONE
            mIvCopyTranslateText.visibility = View.GONE
        }

        mTranslateResultContainer.visibility = View.VISIBLE

        return AlertDialog.Builder(activity, R.style.CommonCustomDialogStyle)
            .setView(root)
            .setCancelable(true)
            .create()
    }

    /***
     * @param type 0: 原， 1：翻译
     */
    fun createSelectLangDialog(
        activity: Activity?,
        type: Int = 1,
        listener: SelectLangListener?
    ): AlertDialog? {
        activity ?: return null
        val root =
            LayoutInflater.from(activity).inflate(R.layout.dialog_select_language, null, false)

        val dialog = activity.let {
            AlertDialog.Builder(it)
                .setView(root)
                .create()
        }

        val dataList = mutableListOf<SelectLangItem>()
        Global.langList.map {
            val item = SelectLangItem()
            item.lang = it
            item.selected = false
            dataList.add(item)
        }

        if (type == 1) {
            dataList.removeAt(0)
        }

        val adapter = SelectLangAdapter(activity, R.layout.item_language, dataList)
        val recyclerView = root.findViewById<MyRecyclerView>(R.id.rvLanguage)
        recyclerView.layoutManager = MyLinearLayoutManager(activity)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        adapter.mItemListener = object : ItemListener {
            override fun onItemClick(position: Int, holder: BaseViewHolder) {
                listener?.onItemSelected(dialog, dataList[position])
            }
        }

        return dialog
    }

    /***
     *
     */
    fun createSelectLangDialog(
        activity: Activity?,
        listener: SelectLangListener?
    ): AlertDialog? {
        activity ?: return null
        val root =
            LayoutInflater.from(activity).inflate(R.layout.dialog_select_language, null, false)

        val dialog = activity.let {
            AlertDialog.Builder(it)
                .setView(root)
                .create()
        }

        val dataList = mutableListOf<SelectLangItem>()
        Global.langList.map {
            val item = SelectLangItem()
            item.lang = it
            item.selected = false
            dataList.add(item)
        }

        dataList.removeAt(0)
        val firstItem = SelectLangItem()
        firstItem.lang = Lang.ALL
        dataList.add(0, firstItem)

        val adapter = SelectLangAdapter(activity, R.layout.item_language, dataList)
        val recyclerView = root.findViewById<MyRecyclerView>(R.id.rvLanguage)
        recyclerView.layoutManager = MyLinearLayoutManager(activity)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        adapter.mItemListener = object : ItemListener {
            override fun onItemClick(position: Int, holder: BaseViewHolder) {
                listener?.onItemSelected(dialog, dataList[position])
            }
        }

        return dialog
    }

    interface SelectLangListener {
        fun onItemSelected(alertDialog: AlertDialog?, data: SelectLangItem)
    }
}