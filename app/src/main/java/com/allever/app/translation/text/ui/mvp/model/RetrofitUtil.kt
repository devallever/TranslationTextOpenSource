package com.allever.app.translation.text.ui.mvp.model

import com.allever.app.translation.text.bean.TranslationBean

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Allever on 2017/1/15.
 */

object RetrofitUtil {
    private const val BASE_URL = "https://translate.google.cn/"
    private val retrofit: Retrofit
    private val retrofitService: RetrofitService

    init {
        val client = OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.SECONDS)
            .build()
        retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    fun translate(
        subscriber: Subscriber<TranslationBean>,
        content: String,
        sl: String,
        tl: String
    ) {
        retrofitService.translate(content, sl = sl, tl = tl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    fun requestTTS(content: String, tl: String, callback: Callback<ResponseBody>) {
        val call =
            retrofitService.requestTTS(q = content, tl = tl, textlen = content.length.toString())
        call.enqueue(callback)
    }
}
