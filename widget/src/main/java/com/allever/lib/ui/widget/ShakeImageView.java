package com.allever.lib.ui.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AnticipateOvershootInterpolator;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author allever
 */
public class ShakeImageView extends AppCompatImageView {

    private ObjectAnimator mObjectAnimator;

    private boolean mLoop = false;

    public ShakeImageView(Context context) {
        this(context, null);
    }

    public ShakeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShakeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mObjectAnimator = ShakeHelper.INSTANCE.createShakeAnimator(this, mLoop);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    public void start(boolean loop) {
        if (mObjectAnimator == null) {
            return;
        }

        mObjectAnimator.cancel();
        mLoop = loop;
        int repeatCount = 0;
        if (mLoop) {
            repeatCount = ValueAnimator.INFINITE;
        }
        mObjectAnimator.setRepeatCount(repeatCount);
        mObjectAnimator.start();
    }

    public void stop() {
        if (mObjectAnimator != null) {
            mObjectAnimator.cancel();
        }
    }
}
