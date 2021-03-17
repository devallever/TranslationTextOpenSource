//package com.android.absbase.ui.widget;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapShader;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.ColorFilter;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.PorterDuffColorFilter;
//import android.graphics.Rect;
//import android.graphics.Shader;
//import android.graphics.Bitmap.Config;
//import android.graphics.Paint.Style;
//import android.graphics.Path.Direction;
//import android.graphics.PorterDuff.Mode;
//import android.graphics.Shader.TileMode;
//import android.graphics.drawable.Drawable;
//import android.util.StateSet;
//import com.android.absbase.utils.DeviceUtils;
//import java.lang.ref.WeakReference;
//import java.util.Arrays;
//
//public class RippleEffect {
//    private static final int MASK_UNKNOWN = -1;
//    private static final int MASK_NONE = 0;
//    private static final int MASK_CONTENT = 1;
//    private static final int MASK_EXPLICIT = 2;
//    public static final int RADIUS_AUTO = -1;
//    private static final int MAX_RIPPLES = 10;
//    public static final long CLICK_DELAY = 250L;
//    private int[] mStateSet;
//    private boolean mRippleActive;
//    private int mExitingRipplesCount;
//    private RippleBackground mBackground;
//    private boolean mBackgroundActive;
//    private Ripple mRipple;
//    private Ripple[] mExitingRipples;
//    private Rect mBounds;
//    private int mColor;
//    private final Rect mTempRect;
//    private final Rect mHotspotBounds;
//    private final Rect mDrawingBounds;
//    private final Rect mDirtyBounds;
//    private float mPendingX;
//    private float mPendingY;
//    private boolean mHasPending;
//    private int mMaxRadius;
//    private float mDensity;
//    private boolean mOverrideBounds;
//    private Paint mRipplePaint;
//    private WeakReference<RippleEffect.Callback> mCallback;
//    private Context mContext;
//    private Drawable mMask;
//    private Bitmap mMaskBuffer;
//    private BitmapShader mMaskShader;
//    private Canvas mMaskCanvas;
//    private Matrix mMaskMatrix;
//    private PorterDuffColorFilter mMaskColorFilter;
//    private boolean mHasValidMask;
//    private boolean mNeedDrawButtonCircle;
//
//    public RippleEffect(Context context) {
//        this.mStateSet = StateSet.WILD_CARD;
//        this.mExitingRipplesCount = 0;
//        this.mColor = 641618017;
//        this.mTempRect = new Rect();
//        this.mHotspotBounds = new Rect();
//        this.mDrawingBounds = new Rect();
//        this.mDirtyBounds = new Rect();
//        this.mMaxRadius = -1;
//        this.mNeedDrawButtonCircle = false;
//        this.mContext = context;
//        this.mDensity = DeviceUtils.getScreenDensity(context);
//    }
//
//    public RippleEffect(Context context, float density) {
//        this.mStateSet = StateSet.WILD_CARD;
//        this.mExitingRipplesCount = 0;
//        this.mColor = 641618017;
//        this.mTempRect = new Rect();
//        this.mHotspotBounds = new Rect();
//        this.mDrawingBounds = new Rect();
//        this.mDirtyBounds = new Rect();
//        this.mMaxRadius = -1;
//        this.mNeedDrawButtonCircle = false;
//        this.mContext = context;
//        this.mDensity = density;
//    }
//
//    public void draw(Canvas canvas) {
//        this.pruneRipples();
//        Rect bounds = this.getDirtyBounds();
//        int saveCount = canvas.save(2);
//        canvas.clipRect(bounds);
//        this.checkAndDrawButtonCircle(canvas, bounds);
//        this.drawContent(canvas);
//        this.drawBackgroundAndRipples(canvas);
//        canvas.restoreToCount(saveCount);
//    }
//
//    private void checkAndDrawButtonCircle(Canvas canvas, Rect bounds) {
//        if (this.mNeedDrawButtonCircle) {
//            int hight = bounds.bottom - bounds.top;
//            int width = bounds.right - bounds.left;
//            int mRadius = Math.min(width, hight) / 2;
//            Path path = new Path();
//            path.addCircle((float)bounds.centerX(), (float)bounds.centerY(), (float)mRadius, Direction.CW);
//            canvas.clipPath(path);
//        }
//
//    }
//
//    private void drawContent(Canvas canvas) {
//    }
//
//    private void pruneRipples() {
//    }
//
//    private Rect getDirtyBounds() {
//        if (!this.isBounded()) {
//            return this.getBounds();
//        } else {
//            Rect drawingBounds = this.mDrawingBounds;
//            Rect dirtyBounds = this.mDirtyBounds;
//            dirtyBounds.set(drawingBounds);
//            drawingBounds.setEmpty();
//            int cX = (int)this.mHotspotBounds.exactCenterX();
//            int cY = (int)this.mHotspotBounds.exactCenterY();
//            Rect rippleBounds = this.mTempRect;
//            Ripple[] activeRipples = this.mExitingRipples;
//            int n = this.mExitingRipplesCount;
//
//            for(int i = 0; i < n; ++i) {
//                activeRipples[i].getBounds(rippleBounds);
//                rippleBounds.offset(cX, cY);
//                drawingBounds.union(rippleBounds);
//            }
//
//            RippleBackground memory_game_background = this.mBackground;
//            if (memory_game_background != null) {
//                memory_game_background.getBounds(rippleBounds);
//                rippleBounds.offset(cX, cY);
//                drawingBounds.union(rippleBounds);
//            }
//
//            dirtyBounds.union(drawingBounds);
//            return dirtyBounds;
//        }
//    }
//
//    private boolean isBounded() {
//        return true;
//    }
//
//    private Rect getBounds() {
//        if (this.mBounds == null) {
//            this.mBounds = new Rect();
//        }
//
//        return this.mBounds;
//    }
//
//    private boolean isProjected() {
//        return false;
//    }
//
//    private void drawBackgroundAndRipples(Canvas canvas) {
//        Ripple active = this.mRipple;
//        RippleBackground memory_game_background = this.mBackground;
//        int count = this.mExitingRipplesCount;
//        if (active != null || count > 0 || memory_game_background != null && memory_game_background.shouldDraw()) {
//            float x = this.mHotspotBounds.exactCenterX();
//            float y = this.mHotspotBounds.exactCenterY();
//            canvas.translate(x, y);
//            this.updateMaskShaderIfNeeded();
//            if (this.mMaskShader != null) {
//                this.mMaskMatrix.setTranslate(-x, -y);
//                this.mMaskShader.setLocalMatrix(this.mMaskMatrix);
//            }
//
//            int color = this.mColor;
//            int halfAlpha = Color.alpha(color) / 2 << 24;
//            Paint p = this.getRipplePaint();
//            if (this.mMaskColorFilter != null) {
//                p.setColor(halfAlpha);
//                p.setColorFilter(this.mMaskColorFilter);
//                p.setShader(this.mMaskShader);
//            } else {
//                int halfAlphaColor = color & 16777215 | halfAlpha;
//                p.setColor(halfAlphaColor);
//                p.setColorFilter((ColorFilter)null);
//                p.setShader((Shader)null);
//            }
//
//            if (memory_game_background != null && memory_game_background.shouldDraw()) {
//                memory_game_background.draw(canvas, p);
//            }
//
//            if (count > 0) {
//                Ripple[] ripples = this.mExitingRipples;
//
//                for(int i = 0; i < count; ++i) {
//                    ripples[i].draw(canvas, p);
//                }
//            }
//
//            if (active != null) {
//                active.draw(canvas, p);
//            }
//
//            canvas.translate(-x, -y);
//        }
//    }
//
//    public void setNeedDrawButtonCircle(boolean mNeedDrawButtonCircle) {
//        this.mNeedDrawButtonCircle = mNeedDrawButtonCircle;
//    }
//
//    private void updateMaskShaderIfNeeded() {
//        if (!this.mHasValidMask) {
//            int maskType = this.getMaskType();
//            if (maskType != -1) {
//                this.mHasValidMask = true;
//                Rect bounds = this.getBounds();
//                if (maskType != 0 && !bounds.isEmpty()) {
//                    if (this.mMaskBuffer != null && this.mMaskBuffer.getWidth() == bounds.width() && this.mMaskBuffer.getHeight() == bounds.height()) {
//                        this.mMaskBuffer.eraseColor(0);
//                    } else {
//                        if (this.mMaskBuffer != null) {
//                            this.mMaskBuffer.recycle();
//                        }
//
//                        this.mMaskBuffer = Bitmap.createBitmap(bounds.width(), bounds.height(), Config.ALPHA_8);
//                        this.mMaskShader = new BitmapShader(this.mMaskBuffer, TileMode.CLAMP, TileMode.CLAMP);
//                        this.mMaskCanvas = new Canvas(this.mMaskBuffer);
//                    }
//
//                    if (this.mMaskMatrix == null) {
//                        this.mMaskMatrix = new Matrix();
//                    } else {
//                        this.mMaskMatrix.reset();
//                    }
//
//                    int fullAlphaColor = this.mColor | -16777216;
//                    this.mMaskColorFilter = new PorterDuffColorFilter(fullAlphaColor, Mode.SRC_IN);
//                    int left = bounds.left;
//                    int top = bounds.top;
//                    this.mMaskCanvas.translate((float)(-left), (float)(-top));
//                    if (maskType == 2) {
//                        this.drawMask(this.mMaskCanvas);
//                    } else if (maskType == 1) {
//                        this.drawContent(this.mMaskCanvas);
//                    }
//
//                    this.mMaskCanvas.translate((float)left, (float)top);
//                } else {
//                    if (this.mMaskBuffer != null) {
//                        this.mMaskBuffer.recycle();
//                        this.mMaskBuffer = null;
//                        this.mMaskShader = null;
//                        this.mMaskCanvas = null;
//                    }
//
//                    this.mMaskMatrix = null;
//                    this.mMaskColorFilter = null;
//                }
//            }
//        }
//    }
//
//    private void drawMask(Canvas canvas) {
//        if (this.mMask != null) {
//            this.mMask.draw(canvas);
//        }
//
//    }
//
//    private int getMaskType() {
//        if (this.mRipple == null && this.mExitingRipplesCount <= 0 && (this.mBackground == null || !this.mBackground.isVisible())) {
//            return -1;
//        } else if (this.mMask != null) {
//            return this.mMask.getOpacity() == -1 ? 0 : 2;
//        } else {
//            return 0;
//        }
//    }
//
//    private Paint getRipplePaint() {
//        if (this.mRipplePaint == null) {
//            this.mRipplePaint = new Paint();
//            this.mRipplePaint.setAntiAlias(true);
//            this.mRipplePaint.setStyle(Style.FILL);
//        }
//
//        return this.mRipplePaint;
//    }
//
//    public boolean setState(int[] stateSet) {
//        if (!Arrays.equals(this.mStateSet, stateSet)) {
//            this.mStateSet = stateSet;
//            return this.onStateChange(stateSet);
//        } else {
//            return false;
//        }
//    }
//
//    public int[] getState() {
//        return this.mStateSet;
//    }
//
//    protected boolean onStateChange(int[] stateSet) {
//        boolean changed = true;
//        boolean enabled = false;
//        boolean pressed = false;
//        boolean focused = false;
//        int[] var6 = stateSet;
//        int var7 = stateSet.length;
//
//        for(int var8 = 0; var8 < var7; ++var8) {
//            int state = var6[var8];
//            if (state == 16842910) {
//                enabled = true;
//            }
//
//            if (state == 16842908) {
//                focused = true;
//            }
//
//            if (state == 16842919) {
//                pressed = true;
//            }
//        }
//
//        this.setRippleActive(enabled && pressed);
//        this.setBackgroundActive(focused || enabled && pressed, focused);
//        return true;
//    }
//
//    protected void setRippleActive(boolean active) {
//        if (this.mRippleActive != active) {
//            this.mRippleActive = active;
//            if (active) {
//                this.tryRippleEnter();
//            } else {
//                this.tryRippleExit();
//            }
//        }
//
//    }
//
//    protected void setBackgroundActive(boolean active, boolean focused) {
//        if (this.mBackgroundActive != active) {
//            this.mBackgroundActive = active;
//            if (active) {
//                this.tryBackgroundEnter(focused);
//            } else {
//                this.tryBackgroundExit();
//            }
//        }
//
//    }
//
//    private void tryBackgroundExit() {
//        if (this.mBackground != null) {
//            this.mBackground.exit();
//        }
//
//    }
//
//    private void tryBackgroundEnter(boolean focused) {
//        if (this.mBackground == null) {
//            this.mBackground = new RippleBackground(this, this.mHotspotBounds);
//        }
//
//        this.mBackground.setup(this.mMaxRadius, this.mDensity);
//        this.mBackground.enter(focused);
//    }
//
//    private void tryRippleEnter() {
//        if (this.mExitingRipplesCount < 10) {
//            if (this.mRipple == null) {
//                float x;
//                float y;
//                if (this.mHasPending) {
//                    this.mHasPending = false;
//                    x = this.mPendingX;
//                    y = this.mPendingY;
//                } else {
//                    x = this.mHotspotBounds.exactCenterX();
//                    y = this.mHotspotBounds.exactCenterY();
//                }
//
//                this.mRipple = new Ripple(this, this.mHotspotBounds, x, y);
//            }
//
//            this.mRipple.setup(this.mMaxRadius, this.mDensity);
//            this.mRipple.enter();
//        }
//    }
//
//    private void tryRippleExit() {
//        if (this.mRipple != null) {
//            if (this.mExitingRipples == null) {
//                this.mExitingRipples = new Ripple[10];
//            }
//
//            this.mExitingRipples[this.mExitingRipplesCount++] = this.mRipple;
//            this.mRipple.exit();
//            this.mRipple = null;
//        }
//
//    }
//
//    protected void onBoundsChange(Rect bounds) {
//        if (!this.mOverrideBounds) {
//            this.mHotspotBounds.set(bounds);
//            this.onHotspotBoundsChanged();
//        }
//
//        this.invalidateSelf();
//    }
//
//    private void onHotspotBoundsChanged() {
//        int count = this.mExitingRipplesCount;
//        Ripple[] ripples = this.mExitingRipples;
//
//        for(int i = 0; i < count; ++i) {
//            ripples[i].onHotspotBoundsChanged();
//        }
//
//        if (this.mRipple != null) {
//            this.mRipple.onHotspotBoundsChanged();
//        }
//
//        if (this.mBackground != null) {
//            this.mBackground.onHotspotBoundsChanged();
//        }
//
//    }
//
//    public void invalidateSelf() {
//        RippleEffect.Callback callback = this.getCallback();
//        if (callback != null) {
//            callback.invalidateDrawable(this);
//        }
//
//    }
//
//    void removeRipple(Ripple ripple) {
//        Ripple[] ripples = this.mExitingRipples;
//        int count = this.mExitingRipplesCount;
//        int index = this.getRippleIndex(ripple);
//        if (index >= 0) {
//            System.arraycopy(ripples, index + 1, ripples, index, count - (index + 1));
//            ripples[count - 1].clear();
//            ripples[count - 1] = null;
//            --this.mExitingRipplesCount;
//            this.invalidateSelf();
//        }
//
//    }
//
//    public void setHotspot(float x, float y) {
//        if (this.mRipple == null || this.mBackground == null) {
//            this.mPendingX = x;
//            this.mPendingY = y;
//            this.mHasPending = true;
//        }
//
//        if (this.mRipple != null) {
//            this.mRipple.move(x, y);
//        }
//
//    }
//
//    public void setBounds(int left, int top, int right, int bottom) {
//        Rect oldBounds = this.mBounds;
//        if (oldBounds == null) {
//            oldBounds = this.mBounds = new Rect();
//        }
//
//        if (oldBounds.left != left || oldBounds.top != top || oldBounds.right != right || oldBounds.bottom != bottom) {
//            if (!oldBounds.isEmpty()) {
//                this.invalidateSelf();
//            }
//
//            this.mBounds.set(left, top, right, bottom);
//            if (this.mMask != null) {
//                this.mMask.setBounds(left, top, right, bottom);
//            }
//
//            this.onBoundsChange(this.mBounds);
//        }
//
//    }
//
//    public void setMask(Drawable drawable) {
//        this.mMask = drawable;
//        if (this.mBounds != null) {
//            this.mHasValidMask = false;
//            this.mMask.setBounds(this.mBounds);
//        }
//
//    }
//
//    public void setHotspotBounds(int left, int top, int right, int bottom) {
//        this.mOverrideBounds = true;
//        this.mHotspotBounds.set(left, top, right, bottom);
//        this.onHotspotBoundsChanged();
//    }
//
//    private int getRippleIndex(Ripple ripple) {
//        Ripple[] ripples = this.mExitingRipples;
//        int count = this.mExitingRipplesCount;
//
//        for(int i = 0; i < count; ++i) {
//            if (ripples[i] == ripple) {
//                return i;
//            }
//        }
//
//        return -1;
//    }
//
//    public void setCallback(RippleEffect.Callback callback) {
//        this.mCallback = new WeakReference(callback);
//    }
//
//    public RippleEffect.Callback getCallback() {
//        return this.mCallback != null ? (RippleEffect.Callback)this.mCallback.get() : null;
//    }
//
//    public void clear() {
//        if (this.mBackground != null) {
//            this.mBackground.clear();
//            this.mBackground = null;
//        }
//
//        if (this.mRipple != null) {
//            this.mRipple.clear();
//            this.mRipple = null;
//        }
//
//    }
//
//    public void setColor(int mColor) {
//        this.mColor = mColor;
//    }
//
//    public void setMaxRadius(int radius) {
//        this.mMaxRadius = radius;
//    }
//
//    public interface Callback {
//        void invalidateDrawable(RippleEffect var1);
//    }
//}
