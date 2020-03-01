package com.allever.app.translation.text.bean

import androidx.annotation.Keep
import com.allever.app.translation.text.function.db.History

@Keep
class BackupBean {
    var data: MutableList<History>? = null
}