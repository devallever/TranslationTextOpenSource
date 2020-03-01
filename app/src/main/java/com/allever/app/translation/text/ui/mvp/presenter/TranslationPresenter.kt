package com.allever.app.translation.text.ui.mvp.presenter

import com.allever.app.translation.text.R
import com.allever.app.translation.text.bean.Lang
import com.allever.app.translation.text.bean.TranslationBean
import com.allever.app.translation.text.bean.UpdateRecordEvent
import com.allever.app.translation.text.function.*
import com.allever.app.translation.text.function.db.DBHelper
import com.allever.app.translation.text.ui.mvp.model.RetrofitUtil
import com.allever.app.translation.text.ui.mvp.view.TranslationView
import com.allever.app.translation.text.util.ClipboardHelper
import com.allever.app.translation.text.util.JsonHelper
import com.allever.lib.common.mvp.BasePresenter
import com.allever.lib.common.util.log
import com.allever.lib.common.util.toast
import org.greenrobot.eventbus.EventBus
import rx.Subscriber

class TranslationPresenter : BasePresenter<TranslationView>() {

    fun translate(content: String, sl: String = Lang.AUTO.CODE, translateLanguage: String) {
        if (content.isEmpty()) {
            toast(R.string.please_input_content)
            return
        }

        val history = DBHelper.getHistory(content, sl, translateLanguage)
        val translationBean =
            JsonHelper.json2Object(history?.result ?: "", TranslationBean::class.java)
        if (translationBean != null) {
            parse(translationBean)
            mViewRef?.get()?.refreshLiked(history?.liked == 1)
            val translateText = TranslationHelper.getTranslateText(translationBean)
            if (translateText.isNotEmpty()) {
                play(translateText, translateLanguage)
                copyToClipBoard(translateText)
            }
            log("获取到数据库翻译内容")
            DBHelper.updateHistoryTime(history)
            EventBus.getDefault().post(UpdateRecordEvent())
            return
        }

        RetrofitUtil.translate(object : Subscriber<TranslationBean>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
                log("失败")
            }

            override fun onNext(bean: TranslationBean) {
                parse(bean)
                val translateText = TranslationHelper.getTranslateText(bean)

                mViewRef?.get()?.refreshLiked(false)

                play(translateText, translateLanguage)

                copyToClipBoard(translateText)

                DBHelper.addHistory(content, sl, translateLanguage, bean)

                EventBus.getDefault().post(UpdateRecordEvent())

            }
        }, content, sl, translateLanguage)
    }

    fun playAudio(content: String, translateLanguage: String) {
        TranslationHelper.playTTS(content, translateLanguage)
    }

    fun shareAudio(obj: Any, content: String, tl: String) {
        TranslationHelper.shareAudio(obj, content, tl)
    }

    fun liked(content: String, sl: String = Lang.AUTO.CODE, tl: String) {
        val result = DBHelper.like(content, sl, tl)
        if (result) {
            val history = DBHelper.getHistory(content, sl, tl)
            val like = history?.liked
            if (like == 0) {
                toast(R.string.added_to_words)
            } else {
                toast(R.string.removed_from_words)
            }
            mViewRef?.get()?.refreshLiked(history?.liked == 1)
            EventBus.getDefault().post(UpdateRecordEvent())
        }
    }

    fun copyText(content: String?) {
        ClipboardHelper.copy(content)
    }

    private fun parse(bean: TranslationBean) {

        val srcSymbol = TranslationHelper.getSrcSymbol(bean)
        val translateSymbol = TranslationHelper.getTranslateSymbol(bean)
        //音标显示逻辑
        if (srcSymbol.isNotEmpty()) {
            mViewRef?.get()?.showOrHideSoundSrcSymbol(true)
        } else {
            mViewRef?.get()?.showOrHideSoundSrcSymbol(false)
        }
        if (translateSymbol.isNotEmpty()) {
            mViewRef?.get()?.showOrHideSoundTranslateSymbol(true)
        } else {
            mViewRef?.get()?.showOrHideSoundTranslateSymbol(false)
        }

        if (TranslationHelper.getDictText(bean).isNotEmpty()) {
            mViewRef?.get()?.showOrHideDictInfo(true)
        } else {
            mViewRef?.get()?.showOrHideDictInfo(false)
        }

        mViewRef?.get()?.updateResult(
            bean,
            TranslationHelper.getSrcLang(bean),
            TranslationHelper.getSrcText(bean),
            TranslationHelper.getSrcSymbol(bean),
            TranslationHelper.getTranslateText(bean),
            TranslationHelper.getTranslateSymbol(bean),
            TranslationHelper.getDictText(bean)
        )
    }

    private fun play(content: String, translateLanguage: String) {
        //播放语音
        if (SettingHelper.getAutoPlayAudio()) {
            playAudio(content, translateLanguage)
        } else {
            TranslationHelper.requestTTS(content, translateLanguage, null)
        }
    }

    private fun copyToClipBoard(translateText: String) {
        //复制剪到贴板
        if (SettingHelper.getCopyClipBoard()) {
            ClipboardHelper.copy(translateText)
        }
    }
}