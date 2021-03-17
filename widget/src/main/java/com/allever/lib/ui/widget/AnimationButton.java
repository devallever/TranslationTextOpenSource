package com.allever.lib.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

/**
 *
 */

/**
 * 一个扩展自TextView的控件，加了自动播放点击动画
 * @author allever
 */
public class AnimationButton extends AppCompatButton {


    public AnimationButton(Context context) {
        super(context);
    }

    public AnimationButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startAnimation(getActionDownAnimation());
                    break;
                case MotionEvent.ACTION_CANCEL:
                    startAnimation(getActionUpAnimation());
                    break;
                case MotionEvent.ACTION_UP:
                    Animation animation = getActionUpAnimation();
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            performClick();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    startAnimation(animation);
                    break;
                default:
                    break;
            }

            return true;

        }
        return false;
    }

    private Animation getActionDownAnimation() {
        ScaleAnimation animation = new ScaleAnimation(1, .9F, 1, .9F, Animation.RELATIVE_TO_SELF, .5F, Animation.RELATIVE_TO_SELF, .5F);
        animation.setDuration(150);
        animation.setFillAfter(true);
        return animation;
    }

    private Animation getActionUpAnimation() {
        ScaleAnimation animation = new ScaleAnimation(.9F, 1, .9F, 1, Animation.RELATIVE_TO_SELF, .5F, Animation.RELATIVE_TO_SELF, .5F);
        animation.setDuration(70);
        return animation;
    }
}
