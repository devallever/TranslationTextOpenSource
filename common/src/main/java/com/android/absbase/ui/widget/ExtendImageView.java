//package com.android.absbase.ui.widget;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Rect;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.util.AttributeSet;
//import android.view.animation.Animation;
//import android.view.animation.Animation.AnimationListener;
//import android.widget.ImageView;
//import com.android.absbase.helper.log.LogUtil;
//import com.android.absbase.ui.widget.drawable.DrawableContainer;
//import com.android.absbase.ui.widget.transform.ImageTransform;
//
//@SuppressLint("AppCompatCustomView")
//public class ExtendImageView extends ImageView {
//    private static final ThreadLocal<Rect> sLocalTmpRect = new ThreadLocal<Rect>() {
//        protected Rect initialValue() {
//            return new Rect();
//        }
//    };
//    private boolean mMeasuredExactly = false;
//    private boolean mBlockMeasurement = false;
//    private boolean mIgnoreContentBounds = false;
//    private boolean mAdjustViewBounds = false;
//    private boolean mPreventForegroundProcessor = false;
//    private ViewForeground mForeground = new ViewForeground(this, (Drawable)null);
//    private Drawable mForegroundDrawable;
//    private Drawable mImageDrawable;
//    private ImageTransform mProcessor;
//    private ScaleType mScaleType;
//
//    public ExtendImageView(Context context) {
//        super(context);
//    }
//
//    public ExtendImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public ExtendImageView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    @Override
//    public void setImageBitmap(Bitmap bm) {
//        this.setImageBitmapInternal(bm);
//    }
//
//    private void setImageBitmapInternal(Bitmap bm) {
//        this.setImageDrawable(new BitmapDrawable(this.getResources(), bm));
//    }
//
//    @Override
//    public void setImageDrawable(Drawable drawable) {
//        this.setImageDrawableInternal(drawable);
//    }
//
//    private void setImageDrawableInternal(Drawable drawable) {
//        this.mBlockMeasurement = true;
//        this.mImageDrawable = drawable;
//        ImageTransform processor = this.mProcessor;
//        drawable = this.processDrawable(processor, drawable);
//        if (drawable != null && drawable instanceof DrawableContainer) {
//            ((DrawableContainer)drawable).setOwnerImageView(this);
//        }
//
//        super.setImageDrawable(drawable);
//        this.mBlockMeasurement = false;
//    }
//
//    protected Drawable processDrawable(ImageTransform processor, Drawable drawable) {
//        if (drawable != null && processor != null) {
//            drawable = processor.doTransform(drawable);
//        }
//
//        return drawable;
//    }
//
//    @Override
//    public void setImageResource(int resId) {
//        this.setImageResourceInternal(resId);
//    }
//
//    private void setImageResourceInternal(int resId) {
//        try {
//            Drawable drawable = this.getResources().getDrawable(resId);
//            this.setImageDrawable(drawable);
//        } catch (Exception var3) {
//            LogUtil.w("ImageView", "Unable to find resource: " + resId, var3);
//        }
//
//    }
//
//    @Override
//    public void setImageURI(Uri uri) {
//        this.setImageURIInternal(uri);
//    }
//
//    private void setImageURIInternal(Uri uri) {
//        this.mBlockMeasurement = true;
//        super.setImageURI(uri);
//        this.mBlockMeasurement = false;
//    }
//
//    public void setImageBitmap(final Bitmap bm, final Animation in, Animation out) {
//        if (out != null) {
//            this.scheduleAnimation(out, new Runnable() {
//                public void run() {
//                    ExtendImageView.this.setImageBitmapInternal(bm);
//                    if (in != null) {
//                        ExtendImageView.this.scheduleAnimation(in, (Runnable)null);
//                    }
//
//                }
//            });
//        } else {
//            this.setImageBitmapInternal(bm);
//            if (in != null) {
//                this.scheduleAnimation(in, (Runnable)null);
//            }
//
//        }
//    }
//
//    public void setImageDrawable(final Drawable drawable, final Animation in, Animation out) {
//        if (out != null) {
//            this.scheduleAnimation(out, new Runnable() {
//                public void run() {
//                    ExtendImageView.this.setImageDrawableInternal(drawable);
//                    if (in != null) {
//                        ExtendImageView.this.scheduleAnimation(in, (Runnable)null);
//                    }
//
//                }
//            });
//        } else {
//            this.setImageDrawableInternal(drawable);
//            if (in != null) {
//                this.scheduleAnimation(in, (Runnable)null);
//            }
//
//        }
//    }
//
//    public void setImageResource(final int resId, final Animation in, Animation out) {
//        if (out != null) {
//            this.scheduleAnimation(out, new Runnable() {
//                public void run() {
//                    ExtendImageView.this.setImageResourceInternal(resId);
//                    if (in != null) {
//                        ExtendImageView.this.scheduleAnimation(in, (Runnable)null);
//                    }
//
//                }
//            });
//        } else {
//            this.setImageResourceInternal(resId);
//            if (in != null) {
//                this.scheduleAnimation(in, (Runnable)null);
//            }
//
//        }
//    }
//
//    public void setImageURI(final Uri uri, final Animation in, Animation out) {
//        if (out != null) {
//            this.scheduleAnimation(out, new Runnable() {
//                public void run() {
//                    ExtendImageView.this.setImageURIInternal(uri);
//                    if (in != null) {
//                        ExtendImageView.this.scheduleAnimation(in, (Runnable)null);
//                    }
//
//                }
//            });
//        } else {
//            this.setImageURIInternal(uri);
//            if (in != null) {
//                this.scheduleAnimation(in, (Runnable)null);
//            }
//
//        }
//    }
//
//    @Override
//    public void setBackgroundDrawable(Drawable drawable) {
//        this.mBlockMeasurement = !this.isBackgroundHasPadding(this.getBackground(), drawable);
//        super.setBackgroundDrawable(drawable);
//        this.mBlockMeasurement = false;
//    }
//
//    @Override
//    public void setBackgroundResource(int resId) {
//        this.mBlockMeasurement = true;
//        super.setBackgroundResource(resId);
//        this.mBlockMeasurement = false;
//    }
//
//    @Override
//    public void setBackgroundColor(int color) {
//        this.mBlockMeasurement = true;
//        super.setBackgroundColor(color);
//        this.mBlockMeasurement = false;
//    }
//
//    private boolean isBackgroundHasPadding(Drawable oldDrawable, Drawable newDrawable) {
//        Rect rect = (Rect)sLocalTmpRect.get();
//        boolean hasPadding = oldDrawable != null && oldDrawable.getPadding(rect);
//        if (!hasPadding) {
//            hasPadding = newDrawable != null && newDrawable.getPadding(rect);
//        }
//
//        return hasPadding;
//    }
//
//    @Override
//    public void requestLayout() {
//        if (!this.mBlockMeasurement || !this.mMeasuredExactly) {
//            super.requestLayout();
//        }
//
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        this.mMeasuredExactly = this.isMeasuredExactly(widthMeasureSpec, heightMeasureSpec);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (this.mIgnoreContentBounds && !this.mAdjustViewBounds) {
//            this.setMeasuredDimension(getDefaultSize(this.getMeasuredWidth(), widthMeasureSpec), getDefaultSize(this.getMeasuredHeight(), heightMeasureSpec));
//        }
//
//    }
//
//    private boolean isMeasuredExactly(int widthMeasureSpec, int heightMeasureSpec) {
//        int widthMeasureSpecMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMeasureSpecMode = MeasureSpec.getMode(heightMeasureSpec);
//        return widthMeasureSpecMode == 1073741824 && heightMeasureSpecMode == 1073741824;
//    }
//
//    public void setForeground(int resId) {
//        this.mBlockMeasurement = true;
//        this.setForeground(resId != 0 ? this.getResources().getDrawable(resId) : null);
//        this.mBlockMeasurement = false;
//    }
//
//    public void setPreventForegroundProcessor(boolean prevent) {
//        if (this.mPreventForegroundProcessor != prevent) {
//            this.mPreventForegroundProcessor = prevent;
//            this.setForeground(this.mForegroundDrawable);
//        }
//
//    }
//
//    @Override
//    public void setForeground(Drawable foregroundDrawable) {
//        this.mBlockMeasurement = true;
//        this.mForegroundDrawable = foregroundDrawable;
//        if (!this.mPreventForegroundProcessor) {
//            ImageTransform processor = this.mProcessor;
//            foregroundDrawable = this.processDrawable(processor, foregroundDrawable);
//        }
//
//        this.mForeground.setDrawable(foregroundDrawable);
//        this.mBlockMeasurement = false;
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        ViewForeground foreground = this.mForeground;
//        if (foreground != null) {
//            foreground.boundsChanged();
//        }
//
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        ViewForeground foreground = this.mForeground;
//        if (foreground != null) {
//            foreground.boundsChanged();
//        }
//
//    }
//
//    @Override
//    protected void drawableStateChanged() {
//        super.drawableStateChanged();
//        ViewForeground foreground = this.mForeground;
//        if (foreground != null) {
//            foreground.drawableStateChanged();
//        }
//
//    }
//
//    @Override
//    protected boolean verifyDrawable(Drawable dr) {
//        ViewForeground foreground = this.mForeground;
//        Drawable foregroundDrawable = foreground == null ? null : foreground.getDrawable();
//        return foregroundDrawable == dr || super.verifyDrawable(dr);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        ViewForeground foreground = this.mForeground;
//        if (foreground != null) {
//            foreground.draw(canvas);
//        }
//
//    }
//
//    @Override
//    public void setAdjustViewBounds(boolean adjustViewBounds) {
//        super.setAdjustViewBounds(adjustViewBounds);
//        this.mAdjustViewBounds = adjustViewBounds;
//    }
//
//    public void setImageProcessor(ImageTransform processor) {
//        if (this.mProcessor != processor) {
//            this.mProcessor = processor;
//            this.handleProcessorChange(processor);
//        }
//
//    }
//
//    private void handleProcessorChange(ImageTransform processor) {
//        ViewForeground foreground = this.mForeground;
//        Drawable imageDrawable;
//        if (foreground != null) {
//            imageDrawable = this.mForegroundDrawable;
//            if (imageDrawable != null) {
//                this.setForeground(imageDrawable);
//            }
//        }
//
//        imageDrawable = this.mImageDrawable;
//        if (imageDrawable != null) {
//            this.setImageDrawable(imageDrawable);
//        }
//
//    }
//
//    public void setIgnoreContentBounds(boolean ignore) {
//        if (this.mIgnoreContentBounds != ignore) {
//            this.mIgnoreContentBounds = ignore;
//            this.requestLayout();
//        }
//
//    }
//
//    private void scheduleAnimation(Animation animation, final Runnable postRunnable) {
//        if (animation == null) {
//            if (postRunnable != null) {
//                postRunnable.run();
//            }
//
//        } else {
//            this.clearAnimation();
//            animation.setAnimationListener(new AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    if (postRunnable != null) {
//                        postRunnable.run();
//                    }
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//                }
//            });
//            this.startAnimation(animation);
//        }
//    }
//}
