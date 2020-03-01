package com.allever.app.translation.text.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.allever.app.translation.text.R
import com.allever.lib.common.app.BaseActivity

class DialogTranslateActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_translate_activity)

        val srcText = intent?.getStringExtra(DialogTranslationFragment.EXTRA_SRC_TEXT) ?: ""
        val fragment = DialogTranslationFragment()
        val bundle = Bundle()
        bundle.putString(DialogTranslationFragment.EXTRA_SRC_TEXT, srcText)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit()
    }

    companion object {
        fun start(context: Context, srcText: String) {
            val intent = Intent(context, DialogTranslateActivity::class.java)
            intent.putExtra(DialogTranslationFragment.EXTRA_SRC_TEXT, srcText)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}