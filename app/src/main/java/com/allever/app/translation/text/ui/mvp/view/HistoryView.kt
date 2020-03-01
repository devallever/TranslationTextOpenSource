package com.allever.app.translation.text.ui.mvp.view

import com.allever.app.translation.text.bean.WordItem

interface HistoryView {
    fun updateWordList(data: MutableList<WordItem>)
}