package com.allever.app.translation.text.function.db

import androidx.annotation.Keep
import org.litepal.crud.LitePalSupport

@Keep
class History : LitePalSupport() {
    var srcText: String = ""
    var sl: String = "en"
    var tl: String = "en"
    var time: Long = 0
    var liked: Int = 0
    //全路径
    var ttsPath: String = ""
    var result: String = ""
}