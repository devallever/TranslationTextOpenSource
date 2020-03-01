package com.allever.app.translation.text.ui.mvp.view

import com.allever.app.translation.text.bean.WordItem

interface WordView {
    fun updateWordList(data: MutableList<WordItem>)
}