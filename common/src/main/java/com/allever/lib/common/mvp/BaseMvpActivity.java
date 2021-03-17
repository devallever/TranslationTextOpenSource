package com.allever.lib.common.mvp;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.allever.lib.common.app.BaseActivity;


/**
 * Created by allever on 18-2-28.
 */

public abstract class BaseMvpActivity<V, P extends BasePresenter<V>> extends BaseActivity {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();
        //view 与 Presenter 关联
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    protected abstract P createPresenter();
}
