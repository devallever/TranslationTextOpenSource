package com.allever.app.translation.text.function

import com.allever.app.translation.text.ui.mvp.model.RetrofitUtil
import com.allever.app.translation.text.util.FileUtils
import com.allever.app.translation.text.util.MD5
import com.allever.lib.common.app.App
import com.allever.lib.common.util.log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object NetworkHelper {

    fun requestTTS(content: String, tl: String, callback: TTSRequestCallback?) {
        RetrofitUtil.requestTTS(content, tl, object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                log("请求语音成功")
                val url = call.request().url().toString()
                log("请求地址：$url")

                val dir = App.context.cacheDir.absolutePath
                val fileName = MD5.getMD5StrToLowerCase("$content$tl") + ".mp3"

                val file = File(dir, fileName)

                FileUtils.saveByteArray2File(response.body()?.bytes(), dir, fileName)

                if (file.exists()) {
                    callback?.onSuccess(file.absolutePath)
                } else {
                    callback?.onFail("保存文件失败")
                    log("文件不存在")
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                log("请求语音失败")
                val url = call.request().url().toString()
                log("请求地址：$url")
            }
        })
    }
}

public interface TTSRequestCallback {
    fun onSuccess(ttsPath: String)
    fun onFail(msg: String = "")
}