package com.allever.lib.recommend

import android.text.TextUtils
import com.allever.lib.common.app.App
import com.allever.lib.common.util.*
import com.google.gson.Gson
import rx.Subscriber
import java.io.File
import java.lang.Exception
import java.util.*

object RecommendGlobal {

    var recommendData = mutableListOf<Recommend>()

    private val DIR =
        App.context.cacheDir.absolutePath + File.separator +"Recommend"

    private const val FILE_NAME = "recommend.json"

    private val FILE_PATH = DIR + File.separator + FILE_NAME

    var channel = ""

    fun init(channel: String) {
        log("cache dir = $DIR")
        FileUtil.createDir(DIR)
        getRecommendData(channel, null)
        this.channel = channel
    }

    fun getRecommendData(channel: String, listener: RecommendListener?) {
        val subscriber = object : Subscriber<RecommendBean>() {
            override fun onNext(data: RecommendBean?) {
                handleRecommendData(data, channel, listener)
                saveRecommend(data)
            }

            override fun onCompleted() {
                log("获取推荐数据成功")
            }

            override fun onError(e: Throwable?) {
                loge(e?.message ?: "")
                log("获取推荐数据失败")

                //todo 获取本地
                try {
                    val content = FileUtil.readFileToString(FILE_PATH)
                    val gson = Gson()
                    val data = gson.fromJson(content, RecommendBean::class.java)
                    handleRecommendData(data, channel, listener)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener?.onFail()                }

            }
        }
        if (SystemUtils.isChineseLang()) {
            RetrofitUtil.getRecommendZh(subscriber)
        } else {
            RetrofitUtil.getRecommendEn(subscriber)
        }

    }

    fun handleRecommendData(data: RecommendBean?, channel: String, listener: RecommendListener?) {
        if (data?.data?.isNotEmpty() == true) {
            recommendData.clear()

            when (channel) {
                "google" -> {
                    handleChannelRecommendData(data, "google")
                }
                "xiaomi" -> {
                    handleChannelRecommendData(data, "xiaomi")
                }
                else -> {
                    data.data?.map {
                        if (it.url.isNotEmpty()) {
                            recommendData.add(it)
                        }
                    }
//                    mRecommendData.addAll(data.data!!)
                }
            }
            listener?.onSuccess(recommendData)
        } else {
            listener?.onFail()
        }
    }

    fun getUrl(pkg: String): String {
        var url = ""
        recommendData.map {
            if (pkg == it.pkg) {
                url = getItemUrl(it)
                return@map
            }
        }

        return url
    }

    fun getItemUrl(item: Recommend): String {

        return when (channel) {
            "google" -> {
                item.googleUrl
            }
            "xiaomi" -> {
                item.xiaomiUrl
            }
            else -> {
                item.url
            }
        }
    }

    fun getRandomItem(): Recommend? {
        if (recommendData.isEmpty()) {
            return null
        }

        val random = Random()
        val index = random.nextInt(recommendData.size)
        log("推荐总数： ${recommendData.size}, 随机: $index")

        return recommendData[index]
    }

    private fun handleChannelRecommendData(data: RecommendBean?, channel: String) {
        data?.data?.map {
            val channelValue = it.channel
            if (!TextUtils.isEmpty(channelValue)) {
                if (channelValue.contains("|")) {
                    // | 分割的多个渠道
                    val channelList = channelValue.split("|")
                    if (channelList.contains(channel)) {
                        recommendData.add(it)
                    }
                } else {
                    //单个渠道值
                    if (channelValue == channel) {
                        recommendData.add(it)
                    }
                }
            }
        }
    }


    private fun saveRecommend(data: RecommendBean?) {
        if (data?.data?.isNotEmpty() == true) {
            val content = Gson().toJson(data)
            FileUtil.saveStringToFile(content, FILE_PATH)
        }
    }


}