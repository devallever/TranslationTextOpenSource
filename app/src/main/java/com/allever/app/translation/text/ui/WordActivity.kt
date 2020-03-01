package com.allever.app.translation.text.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import com.allever.app.translation.text.R




import com.allever.lib.common.app.BaseActivity
import com.allever.lib.common.app.BaseFragment
import com.allever.lib.common.util.ActivityCollector
import kotlinx.android.synthetic.main.activity_word.*
import kotlinx.android.synthetic.main.include_top_bar.*

class WordActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mFragment: BaseFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word)

        initView()
        initData()
    }

    private fun initView() {
        tv_label.text = getString(R.string.title_words)
        iv_left.setOnClickListener(this)
        iv_right.setImageResource(R.drawable.ic_history)
        iv_right.setOnClickListener(this)
        iv_right.visibility = View.VISIBLE
    }

    private fun initData() {
        val transaction = supportFragmentManager.beginTransaction()
        mFragment = WordFragment.newInstance()
        transaction.replace(R.id.fragmentContainer, mFragment)
        transaction.commit()


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_left -> {
                onKeyDown(0, null)
            }
            R.id.iv_right -> {
                ActivityCollector.startActivity(this, HistoryActivity::class.java)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val result = mFragment.onKeyDown(keyCode, event)
        if (!result) {
            finish()
        }

        return result
    }









}