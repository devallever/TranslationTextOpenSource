package com.allever.app.translation.text.ui.mvp.view

import com.allever.app.translation.text.bean.TranslationBean

interface TranslationView {
    fun updateResult(
        data: TranslationBean,
        srcLang: String,
        srcText: String,
        srcSymbol: String,
        translateText: String,
        translateSymbol: String,
        dictText: String
    )

    fun refreshLiked(liked: Boolean)

    fun showOrHideSoundSrcSymbol(show: Boolean)
    fun showOrHideSoundTranslateSymbol(show: Boolean)
    fun showOrHideDictInfo(show: Boolean)
}