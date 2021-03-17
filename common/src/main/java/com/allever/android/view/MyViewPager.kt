package com.allever.android.view

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class MyViewPager: ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    interface OnPageChangeListener: ViewPager.OnPageChangeListener

}