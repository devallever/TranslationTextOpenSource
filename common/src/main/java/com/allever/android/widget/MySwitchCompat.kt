package com.allever.android.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat

class MySwitchCompat: SwitchCompat {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
}