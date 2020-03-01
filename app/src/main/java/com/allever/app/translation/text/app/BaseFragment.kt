package com.allever.app.translation.text.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.allever.lib.common.mvp.BaseMvpFragment
import com.allever.lib.common.mvp.BasePresenter

abstract class BaseFragment<V, P : BasePresenter<V>> : BaseMvpFragment<V, P>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = LayoutInflater.from(context).inflate(getContentView(), container, false);
        initView(view)
        initData()
        return view
    }

    abstract fun getContentView(): Int
    abstract fun initView(root: View)
    abstract fun initData()
}