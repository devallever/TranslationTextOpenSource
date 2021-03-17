package com.allever.android.app

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


abstract class MyFragmentPagerAdapter: FragmentPagerAdapter {
    constructor(fm: FragmentManager) : super(
        fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    )
}