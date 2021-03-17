package com.allever.lib.ui.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator

object ShakeHelper {

    fun createShakeAnimator(target: View, loop: Boolean = false): ObjectAnimator {

        val objectAnimator = ObjectAnimator.ofFloat(
            target, "rotation",
            0f, 5f, 0f, -5f, 0f, 7f, 0f, -7f, 0f, 5f, 3f, 0f, 0f, 0f, 0f, 0f, 0f, 0f
        )
        objectAnimator.duration = 3000
        objectAnimator.interpolator = AnticipateOvershootInterpolator()
        objectAnimator.startDelay = 1000
        var repeatCount = 0
        if (loop) {
            repeatCount = ValueAnimator.INFINITE
        }
        objectAnimator.repeatCount = repeatCount

        return objectAnimator
    }
}
