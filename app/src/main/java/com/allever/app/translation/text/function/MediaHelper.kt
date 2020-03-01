package com.allever.app.translation.text.function

import android.media.MediaPlayer
import com.allever.app.translation.text.bean.PlayAudioFinishEvent
import com.allever.lib.common.util.log
import org.greenrobot.eventbus.EventBus

object MediaHelper {

    private var mPlayer: MediaPlayer? = null

    private var mIsPlaying = false
    fun playFile(path: String) {
        try {
            if (mPlayer?.isPlaying == true) {
                mPlayer?.stop()

            }
            mPlayer?.release()
            mPlayer = null
            mPlayer = MediaPlayer()
            mPlayer?.setDataSource(path)
            mPlayer?.setOnPreparedListener {
                it.start()
                mIsPlaying = true
                log("开始播放")
            }
            mPlayer?.prepare()
            mPlayer?.setOnCompletionListener {
                log("播放完成")
                mIsPlaying = false
                EventBus.getDefault().post(PlayAudioFinishEvent())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isPlaying(): Boolean {
        return mPlayer?.isPlaying == true
    }
}