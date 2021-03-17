package com.allever.lib.recommend

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by Allever on 2017/1/15.
 */

interface RetrofitService {
    @GET("data/allever/recommend/recommend.zh.json")
    fun getRecommendZh(): Observable<RecommendBean>

    @GET("data/allever/recommend/recommend.en.json")
    fun getRecommendEn(): Observable<RecommendBean>
}
