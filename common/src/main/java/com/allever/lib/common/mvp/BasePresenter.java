package com.allever.lib.common.mvp;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by allever on 18-2-28.
 */

public abstract class BasePresenter<V> {
    protected Reference<V> mViewRef;//View类(Activity Fragment, View(控件))接口弱引用

    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    protected V getView() {
        return mViewRef.get();
    }

    public boolean isAttachedView() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
