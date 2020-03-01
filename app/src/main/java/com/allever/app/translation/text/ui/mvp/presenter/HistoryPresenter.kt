package com.allever.app.translation.text.ui.mvp.presenter

import com.allever.app.translation.text.bean.LikeUpdateEvent
import com.allever.app.translation.text.bean.UpdateRecordEvent
import com.allever.app.translation.text.bean.WordItem
import com.allever.app.translation.text.function.db.DBHelper
import com.allever.app.translation.text.function.db.History
import com.allever.app.translation.text.ui.mvp.view.HistoryView
import com.allever.lib.common.mvp.BasePresenter
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal

class HistoryPresenter : BasePresenter<HistoryView>() {
    fun getHistory(sl: String, tl: String) {
        run {
            try {
                //四种情况
                var historyList = mutableListOf<History>()
                if (sl.isEmpty() && tl.isEmpty()) {
                    //查所有
                    historyList = (LitePal.order("time desc").find(History::class.java))
                }

                if (sl.isNotEmpty() && tl.isNotEmpty()) {
                    historyList = LitePal
                        .where("sl = ? and tl = ?", sl, tl)
                        .order("time desc")
                        .find(History::class.java)
                }

                if (sl.isEmpty() && tl.isNotEmpty()) {
                    historyList = LitePal
                        .where("tl = ?", tl)
                        .order("time desc")
                        .find(History::class.java)
                }

                if (sl.isNotEmpty() && tl.isEmpty()) {
                    historyList = LitePal
                        .where("sl = ?", sl)
                        .order("time desc")
                        .find(History::class.java)
                }
                val wordItemList = mutableListOf<WordItem>()
                historyList.map {
                    val wordItem = WordItem()
                    wordItem.history = it
                    wordItem.checked = false
                    wordItemList.add(wordItem)
                }
                mViewRef?.get()?.updateWordList(wordItemList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


//    @Deprecated("")
//    fun getHistory() {
//        run {
//            try {
//                val historyList = LitePal.order("time desc").find(History::class.java)
//                mViewRef?.get()?.updateWordList(historyList)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    fun remove(history: History?) {
        run {
            history?.delete()
            EventBus.getDefault().post(UpdateRecordEvent())
        }
    }

    fun removeHistories(wordItemSet: MutableSet<WordItem>?) {
        wordItemSet ?: return
        wordItemSet.map {
            it.history?.delete()
        }
        EventBus.getDefault().post(UpdateRecordEvent())
    }

    fun liked(history: History?) {
        history ?: return
        val result = DBHelper.like(history)
        if (result) {
            val newHistory = DBHelper.getHistory(history.srcText, history.sl, history.tl)
            if (newHistory != null) {
                EventBus.getDefault().post(LikeUpdateEvent(newHistory))
            }
        } else {
        }
    }
}