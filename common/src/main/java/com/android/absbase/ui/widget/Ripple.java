//package com.android.absbase.ui.widget;
//
//import android.animation.Animator;
//import android.animation.ObjectAnimator;
//import android.animation.Animator.AnimatorListener;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.view.animation.Interpolator;
//import android.view.animation.LinearInterpolator;
//import com.android.absbase.utils.MathUtils;
//
//public class Ripple {
//    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
//    private static final Interpolator DECEL_INTERPOLATOR = new Ripple.CustomInterpolator();
//    private static final float GLOBAL_SPEED = 1.0F;
//    private static final float WAVE_TOUCH_DOWN_ACCELERATION = 1024.0F;
//    private static final float WAVE_TOUCH_UP_ACCELERATION = 1024.0F;
//    private static final float WAVE_OPACITY_DECAY_VELOCITY = 3.0F;
//    private static final long RIPPLE_ENTER_DELAY = 80L;
//    private static final int DIV_Y = 24;
//    public static final int DEFALUT_COLOR = 641618017;
//    private final RippleEffect mOwner;
//    private final Rect mBounds;
//    private float mOuterRadius;
//    private float mDensity;
//    private float mStartingX;
//    private float mStartingY;
//    private float mClampedStartingX;
//    private float mClampedStartingY;
//    private ObjectAnimator mAnimRadius;
//    private ObjectAnimator mAnimOpacity;
//    private ObjectAnimator mAnimX;
//    private ObjectAnimator mAnimY;
//    private Paint mTempPaint;
//    private float mOpacity = 1.0F;
//    private float mOuterX;
//    private float mOuterY;
//    private float mTweenRadius = 0.0F;
//    private float mTweenX = 0.0F;
//    private float mTweenY = 0.0F;
//    private boolean mHardwareAnimating;
//    private boolean mCanUseHardware;
//    private boolean mHasMaxRadius;
//    private boolean mCanceled;
//    private boolean mHasPendingHardwareExit;
//    private int mPendingRadiusDuration;
//    private int mPendingOpacityDuration;
//    private final AnimatorListener mAnimationListener = new AnimatorListener() {
//        @Override
//        public void onAnimationEnd(Animator animation) {
//            Ripple.this.removeSelf();
//        }
//
//        @Override
//        public void onAnimationCancel(Animator animation) {
//        }
//
//        @Override
//        public void onAnimationRepeat(Animator animation) {
//        }
//
//        @Override
//        public void onAnimationStart(Animator animation) {
//        }
//    };
//
//    public Ripple(RippleEffect owner, Rect bounds, float startingX, float startingY) {
//        this.mOwner = owner;
//        this.mBounds = bounds;
//        this.mStartingX = startingX;
//        this.mStartingY = startingY;
//    }
//
//    public void setup(int maxRadius, float density) {
//        if (maxRadius != -1) {
//            this.mHasMaxRadius = true;
//            this.mOuterRadius = (float)maxRadius;
//        } else {
//            float halfWidth = (float)this.mBounds.width() / 2.0F;
//            float halfHeight = (float)this.mBounds.height() / 2.0F;
//            this.mOuterRadius = (float)Math.sqrt((double)(halfWidth * halfWidth + halfHeight * halfHeight));
//        }
//
//        this.mOuterX = 0.0F;
//        this.mOuterY = 0.0F;
//        this.mDensity = density;
//        this.clampStartingPosition();
//    }
//
//    public boolean isHardwareAnimating() {
//        return this.mHardwareAnimating;
//    }
//
//    private void clampStartingPosition() {
//        float cX = this.mBounds.exactCenterX();
//        float cY = this.mBounds.exactCenterY();
//        float dX = this.mStartingX - cX;
//        float dY = this.mStartingY - cY;
//        float r = this.mOuterRadius;
//        if (dX * dX + dY * dY > r * r) {
//            double angle = Math.atan2((double)dY, (double)dX);
//            this.mClampedStartingX = cX + (float)(Math.cos(angle) * (double)r);
//            this.mClampedStartingY = cY + (float)(Math.sin(angle) * (double)r);
//        } else {
//            this.mClampedStartingX = this.mStartingX;
//            this.mClampedStartingY = this.mStartingY;
//        }
//
//    }
//
//    public void onHotspotBoundsChanged() {
//        if (!this.mHasMaxRadius) {
//            float halfWidth = (float)this.mBounds.width() / 2.0F;
//            float halfHeight = (float)this.mBounds.height() / 2.0F;
//            this.mOuterRadius = (float)Math.sqrt((double)(halfWidth * halfWidth + halfHeight * halfHeight));
//            this.clampStartingPosition();
//        }
//
//    }
//
//    public void setOpacity(float a) {
//        this.mOpacity = a;
//        this.invalidateSelf();
//    }
//
//    public float getOpacity() {
//        return this.mOpacity;
//    }
//
//    public void setRadiusGravity(float r) {
//        this.mTweenRadius = r;
//        this.invalidateSelf();
//    }
//
//    public float getRadiusGravity() {
//        return this.mTweenRadius;
//    }
//
//    public void setXGravity(float x) {
//        this.mTweenX = x;
//        this.invalidateSelf();
//    }
//
//    public float getXGravity() {
//        return this.mTweenX;
//    }
//
//    public void setYGravity(float y) {
//        this.mTweenY = y;
//        this.invalidateSelf();
//    }
//
//    public float getYGravity() {
//        return this.mTweenY;
//    }
//
//    public boolean draw(Canvas c, Paint p) {
//        boolean hasContent = false;
//        hasContent = this.drawSoftware(c, p);
//        return hasContent;
//    }
//
//    private boolean drawSoftware(Canvas canvas, Paint p) {
//        boolean hasContent = false;
//        int paintAlpha = p.getAlpha();
//        int alpha = (int)((float)paintAlpha * this.mOpacity + 0.5F);
//        float radius = MathUtils.lerp(0.0F, this.mOuterRadius, this.mTweenRadius);
//        if (alpha > 0 && radius > 0.0F) {
//            float x = MathUtils.lerp(this.mClampedStartingX - this.mBounds.exactCenterX(), this.mOuterX, this.mTweenX);
//            float y = MathUtils.lerp(this.mClampedStartingY - this.mBounds.exactCenterY(), this.mOuterY, this.mTweenY);
//            p.setAlpha(alpha);
//            canvas.drawCircle(x, y, radius, p);
//            p.setAlpha(paintAlpha);
//            hasContent = true;
//        }
//
//        return hasContent;
//    }
//
//    public void getBounds(Rect bounds) {
//        int outerX = (int)this.mOuterX;
//        int outerY = (int)this.mOuterY;
//        int r = (int)this.mOuterRadius + 1;
//        bounds.set(outerX - r, outerY - r, outerX + r, outerY + r);
//    }
//
//    public void move(float x, float y) {
//        this.mStartingX = x;
//        this.mStartingY = y;
//        this.clampStartingPosition();
//    }
//
//    public void enter() {
//        this.cancel();
//        int radiusDuration = (int)(1000.0D * Math.sqrt((double)(this.mOuterRadius / 1024.0F * this.mDensity)) + 0.5D);
//        ObjectAnimator radius = ObjectAnimator.ofFloat(this, "RadiusGravity", new float[]{1.0F});
//        radius.setDuration((long)radiusDuration);
//        radius.setInterpolator(LINEAR_INTERPOLATOR);
//        radius.setStartDelay(80L);
//        ObjectAnimator cX = ObjectAnimator.ofFloat(this, "XGravity", new float[]{1.0F});
//        cX.setDuration((long)radiusDuration);
//        cX.setInterpolator(LINEAR_INTERPOLATOR);
//        cX.setStartDelay(80L);
//        ObjectAnimator cY = ObjectAnimator.ofFloat(this, "YGravity", new float[]{1.0F});
//        cY.setDuration((long)radiusDuration);
//        cY.setInterpolator(LINEAR_INTERPOLATOR);
//        cY.setStartDelay(80L);
//        this.mAnimRadius = radius;
//        this.mAnimX = cX;
//        this.mAnimY = cY;
//        radius.start();
//        cX.start();
//        cY.start();
//    }
//
//    public void exit() {
//        float radius = MathUtils.lerp(0.0F, this.mOuterRadius, this.mTweenRadius);
//        float remaining;
//        if (this.mAnimRadius != null && this.mAnimRadius.isRunning()) {
//            remaining = this.mOuterRadius - radius;
//        } else {
//            remaining = this.mOuterRadius;
//        }
//
//        this.cancel();
//        int radiusDuration = (int)(1000.0D * Math.sqrt((double)(remaining / 2048.0F * this.mDensity)) + 0.5D);
//        int opacityDuration = (int)(1000.0F * this.mOpacity / 3.0F + 0.5F);
//        this.exitSoftware(radiusDuration, opacityDuration);
//    }
//
//    private void exitSoftware(int radiusDuration, int opacityDuration) {
//        ObjectAnimator radiusAnim = ObjectAnimator.ofFloat(this, "RadiusGravity", new float[]{1.0F});
//        radiusAnim.setDuration((long)radiusDuration);
//        radiusAnim.setInterpolator(DECEL_INTERPOLATOR);
//        ObjectAnimator xAnim = ObjectAnimator.ofFloat(this, "XGravity", new float[]{1.0F});
//        xAnim.setDuration((long)radiusDuration);
//        xAnim.setInterpolator(DECEL_INTERPOLATOR);
//        ObjectAnimator yAnim = ObjectAnimator.ofFloat(this, "YGravity", new float[]{1.0F});
//        yAnim.setDuration((long)radiusDuration);
//        yAnim.setInterpolator(DECEL_INTERPOLATOR);
//        ObjectAnimator opacityAnim = ObjectAnimator.ofFloat(this, "Opacity", new float[]{0.0F});
//        opacityAnim.setDuration((long)opacityDuration);
//        opacityAnim.setInterpolator(LINEAR_INTERPOLATOR);
//        opacityAnim.addListener(this.mAnimationListener);
//        this.mAnimRadius = radiusAnim;
//        this.mAnimOpacity = opacityAnim;
//        this.mAnimX = xAnim;
//        this.mAnimY = yAnim;
//        radiusAnim.start();
//        opacityAnim.start();
//        xAnim.start();
//        yAnim.start();
//    }
//
//    public void jump() {
//        this.mCanceled = true;
//        this.endSoftwareAnimations();
//        this.cancelHardwareAnimations(true);
//        this.mCanceled = false;
//    }
//
//    private void endSoftwareAnimations() {
//        if (this.mAnimRadius != null) {
//            this.mAnimRadius.end();
//            this.mAnimRadius = null;
//        }
//
//        if (this.mAnimOpacity != null) {
//            this.mAnimOpacity.end();
//            this.mAnimOpacity = null;
//        }
//
//        if (this.mAnimX != null) {
//            this.mAnimX.end();
//            this.mAnimX = null;
//        }
//
//        if (this.mAnimY != null) {
//            this.mAnimY.end();
//            this.mAnimY = null;
//        }
//
//    }
//
//    public void cancel() {
//        this.mCanceled = true;
//        this.cancelSoftwareAnimations();
//        this.cancelHardwareAnimations(false);
//        this.mCanceled = false;
//    }
//
//    private void cancelSoftwareAnimations() {
//        if (this.mAnimRadius != null) {
//            this.mAnimRadius.cancel();
//            this.mAnimRadius = null;
//        }
//
//        if (this.mAnimOpacity != null) {
//            this.mAnimOpacity.cancel();
//            this.mAnimOpacity = null;
//        }
//
//        if (this.mAnimX != null) {
//            this.mAnimX.cancel();
//            this.mAnimX = null;
//        }
//
//        if (this.mAnimY != null) {
//            this.mAnimY.cancel();
//            this.mAnimY = null;
//        }
//
//    }
//
//    private void cancelHardwareAnimations(boolean jumpToEnd) {
//        this.mHardwareAnimating = false;
//    }
//
//    private void removeSelf() {
//        if (!this.mCanceled) {
//            this.mOwner.removeRipple(this);
//        }
//
//    }
//
//    private void invalidateSelf() {
//        this.mOwner.invalidateSelf();
//    }
//
//    public void clear() {
//    }
//
//    public static final class CustomInterpolator implements Interpolator {
//        public CustomInterpolator() {
//        }
//
//        @Override
//        public float getInterpolation(float input) {
//            return 1.0F - (float)Math.pow(400.0D, (double)(-input) * 1.4D);
//        }
//    }
//}
