package com.allever.lib.recommend

interface RecommendListener {
    fun onSuccess(data: MutableList<Recommend>)
    fun onFail()
}