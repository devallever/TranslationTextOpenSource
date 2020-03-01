package com.allever.app.translation.text.function.db

import com.allever.app.translation.text.bean.RemoveLikesEvent
import com.allever.app.translation.text.bean.TranslationBean
import com.allever.app.translation.text.util.MD5
import com.allever.lib.common.util.log
import com.allever.lib.common.util.loge
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal
import java.io.File

object DBHelper {

    fun updateHistoryTime(history: History?) {
        history?.time = System.currentTimeMillis()
        history?.saveOrUpdate(
            "srcText = ? and sl = ? and tl = ?",
            history.srcText,
            history.sl,
            history.tl
        )
    }

    fun addHistory(content: String, sl: String, tl: String, bean: TranslationBean) {
        run {
            try {
                val history = History()
                history.srcText = content
                history.sl = sl
                history.tl = tl
                history.time = System.currentTimeMillis()
                history.liked = 0
                history.result = Gson().toJson(bean)
                history.ttsPath = MD5.getMD5StrToLowerCase("$content$tl.mp3")
                val saveResult = history.save()
                if (saveResult) {
                    log("保存翻译成功")
                } else {
                    loge("保存翻译失败")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                loge("保存翻译失败")
            }
        }
    }

    fun addHistory(content: String, sl: String, tl: String, result: String) {
        run {
            try {
                val history = History()
                history.srcText = content
                history.sl = sl
                history.tl = tl
                history.time = System.currentTimeMillis()
                history.liked = 0
                history.result = Gson().toJson(result)
                history.ttsPath = MD5.getMD5StrToLowerCase("$content$tl.mp3")
                val saveResult = history.save()
                if (saveResult) {
                    log("保存翻译成功")
                } else {
                    loge("保存翻译失败")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                loge("保存翻译失败")
            }
        }
    }

    fun getHistory(content: String, sl: String, tl: String): History? {
        try {
            val historyList =
                LitePal.where("srcText = ? and sl = ? and tl = ?", content, sl, tl)
                    .find(History::class.java)
            if (historyList.isNotEmpty()) {
                return historyList[0]
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getAllHistory(): MutableList<History> {
        return LitePal.findAll(History::class.java)
    }

    fun like(history: History): Boolean {
        return like(history.srcText, history.sl, history.tl)
    }

    fun like(history: History?, liked: Boolean): Boolean {
        history ?: return false
        return like(history.srcText, history.sl, history.tl, liked)
    }

    fun like(content: String, sl: String, tl: String, liked: Boolean): Boolean {
        val history = getHistory(content, sl, tl)
        if (liked) {
            history?.liked = 1
        } else {
            history?.liked = 0
        }
        val result =
            history?.saveOrUpdate("srcText = ? and sl = ? and tl = ?", content, sl, tl) ?: false
        if (result) {
            log("更新liked成功")
        } else {
            log("更新liked失败")
        }
        return result
    }

    fun like(content: String, sl: String, tl: String): Boolean {
        val history = getHistory(content, sl, tl)
        val like = history?.liked
        if (like == 0) {
            history.liked = 1
        } else {
            history?.liked = 0
        }
        val result =
            history?.saveOrUpdate("srcText = ? and sl = ? and tl = ?", content, sl, tl) ?: false
        if (result) {
            log("更新liked成功")
        } else {
            log("更新liked失败")
        }
        return result
    }

    fun removeLikes(historyList: MutableList<History>) {
        historyList.map {
            val history = getHistory(it.srcText, it.sl, it.tl)
            history?.liked = 0
            history?.saveOrUpdate("srcText = ? and sl = ? and tl = ?", it.srcText, it.sl, it.tl)
        }
        EventBus.getDefault().post(RemoveLikesEvent(historyList))
    }

    fun saveTTS(content: String, tl: String, path: String) {
        val tts = TTS()
        tts.content = content
        tts.tl = tl
        tts.path = path
        val ttsResult = tts.save()
        if (ttsResult) {
            log("保存语音记录成功")
        } else {
            loge("保存语音记录失败")
        }
    }

    fun getTTSPath(content: String, tl: String): String {
        val ttsList = LitePal.where("content = ? and tl = ?", content, tl)
            .find(TTS::class.java)
        var path = ""
        if (ttsList.isNotEmpty()) {
            val tts = ttsList[0]
            path = tts.path
            val file = File(path)
            path = if (file.exists()) {
                path
            } else {
                log("TTS文件不存在")
                ""
            }
        }

        log("TTS文件路径 = $path")
        return path
    }
}