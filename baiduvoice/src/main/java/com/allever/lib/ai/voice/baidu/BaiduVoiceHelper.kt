package com.allever.lib.ai.voice.baidu

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import com.allever.lib.common.app.App
import com.allever.lib.common.util.log
import com.allever.lib.common.util.loge
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import org.json.JSONObject
import java.util.LinkedHashMap

object BaiduVoiceHelper : EventListener {

    private var mEventManager: EventManager? = null
    private var mRecognizedListenerList = mutableListOf<RecognizedListener>()
    private var mRecognizedType: RecognizedType = RecognizedType.CHINESE
//    private var mListener: RecognizedListener? = null

    //重新识别
    private var mNeedReRecognized = true

    fun init() {
        mEventManager = EventManagerFactory.create(App.context, "asr")
        mEventManager?.registerListener(this)
    }

    fun setRecognizedListener(recognizedListener: RecognizedListener) {
        mRecognizedListenerList.add(recognizedListener)
    }

    fun removeRecognised(recognizedListener: RecognizedListener) {
        mRecognizedListenerList.remove(recognizedListener)
    }

    fun startRecognize(type: RecognizedType = mRecognizedType) {
        mNeedReRecognized = true
        mRecognizedType = type
        val params = LinkedHashMap<String, Any>()
        var event: String? = null
        event = SpeechConstant.ASR_START // 替换成测试的event

        // 基于SDK集成2.1 设置识别参数
        params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        // params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        if (mRecognizedType == RecognizedType.ENGLISH) {
            params[SpeechConstant.PID] = 1737 //英语
        } else {
            params[SpeechConstant.PID] = 15362 //普通话搜索模型, 默认
        }


        /* 语音自训练平台特有参数 */
        // params.put(SpeechConstant.PID, 8002);
        // 语音自训练平台特殊pid，8002：搜索模型类似开放平台 1537  具体是8001还是8002，看自训练平台页面上的显示
        // params.put(SpeechConstant.LMID,1068); // 语音自训练平台已上线的模型ID，https://ai.baidu.com/smartasr/model
        // 注意模型ID必须在你的appId所在的百度账号下
        /* 语音自训练平台特有参数 */

        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        AutoCheck(App.context, @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 100) {
                    val autoCheck = msg.obj as AutoCheck
                    synchronized(autoCheck) {
                        val message =
                            autoCheck.obtainErrorMessage() // autoCheck.obtainAllMessage();
//                        txtLog.append(message + "\n")
                        log(message)
                        // Log.w("AutoCheckMessage", message);
                    }// 可以用下面一行替代，在logcat中查看代码
                }
            }
        }, false).checkAsr(params)
        var json: String? = null // 可以替换成自己的json
        json = JSONObject(params as Map<*, *>).toString() // 这里可以替换成你需要测试的json
        mEventManager?.send(event, json, null, 0, 0)
        log("开始识别: 输入参数：$json")
    }

    fun stopRecognize() {
        mNeedReRecognized = false
        log("停止识别：ASR_STOP")
        mEventManager?.send(SpeechConstant.ASR_STOP, null, null, 0, 0) //
    }

    fun destroy() {
        stopRecognize()
        mEventManager?.unregisterListener(this)
    }

    // 基于sdk集成1.2 自定义输出事件类 EventListener 回调方法
    // 基于SDK集成3.1 开始回调事件
    override fun onEvent(
        name: String,
        params: String?,
        data: ByteArray?,
        offset: Int,
        length: Int
    ) {
        var logTxt = "name: $name"


        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :$params"
        }
        if (data != null) {
            logTxt += " ;data length=" + data.size
        }
        log(logTxt)

        if (params?.contains("final_result") == true) {
            loge("最后识别结果: $logTxt")
            try {
                val jsonObject = JSONObject(params)
                val result = jsonObject.getString("best_result")?:""
                mRecognizedListenerList.map {
                    it.onResult(params, result)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (name == "asr.exit" && mNeedReRecognized) {
            startRecognize()
            //重新识别
        }
    }
}