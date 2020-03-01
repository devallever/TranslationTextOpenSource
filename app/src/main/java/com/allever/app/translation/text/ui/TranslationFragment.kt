package com.allever.app.translation.text.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.allever.app.translation.text.R

import com.allever.app.translation.text.app.BaseFragment
import com.allever.app.translation.text.app.Global
import com.allever.app.translation.text.bean.*
import com.allever.app.translation.text.function.SettingHelper
import com.allever.app.translation.text.ui.dialog.DialogHelper
import com.allever.app.translation.text.ui.mvp.presenter.TranslationPresenter
import com.allever.app.translation.text.ui.mvp.view.TranslationView



import com.allever.lib.ai.voice.baidu.BaiduVoiceHelper
import com.allever.lib.ai.voice.baidu.RecognizedListener
import com.allever.lib.ai.voice.baidu.RecognizedType
import kotlinx.android.synthetic.main.include_select_language_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TranslationFragment : BaseFragment<TranslationView, TranslationPresenter>(), TranslationView,
    View.OnClickListener, RecognizedListener {

    private lateinit var mInputContainer: ViewGroup
    private lateinit var mEtContent: EditText
    //    private lateinit var mBtnTranslate: View
    private lateinit var mTvResult: TextView
    private lateinit var mIvChangeLanguage: ImageView
    private lateinit var mTvSrcLanguage: TextView
    private lateinit var mTvTranslateLanguage: TextView

    private lateinit var mTvSoundSrcLanguage: TextView
    private lateinit var mTvSrcText: TextView
    private lateinit var mTvSrcSymbol: TextView
    private lateinit var mIvSrcSound: ImageView
    private lateinit var mIvLiked: ImageView

    private lateinit var mTvSoundTranslateLanguage: TextView
    private lateinit var mTvTranslateText: TextView
    private lateinit var mTvTranslateSymbol: TextView
    private lateinit var mIvTranslateSound: ImageView

    private lateinit var mCardDictPanel: ViewGroup

    private lateinit var mIvClose: ImageView

    private lateinit var mLlResultContainer: ViewGroup
    private var mSelectTranslateLangDialog: AlertDialog? = null
    private var mSelectSrcLangDialog: AlertDialog? = null
    private val mDefaultLang = Lang.CHINESE

    private val mContent: String
        get() {
            val value = mEtContent.text.toString()
            if (value.isEmpty()) {
                setVisibility(mLlResultContainer, false)
                mLlResultContainer.visibility = View.GONE
            } else {
                setVisibility(mLlResultContainer, true)
            }
            return value
        }


    private lateinit var mIvRecognized: ImageView
    private var mRecognizing = false
    private var mNeedRestartAudioRecognize = false

    override fun getContentView(): Int = R.layout.fragment_translation

    override fun initView(root: View) {
        EventBus.getDefault().register(this)

        BaiduVoiceHelper.init()

        mInputContainer = root.findViewById(R.id.inputContainer)
        mEtContent = root.findViewById(R.id.etInputContent)
        mIvClose = root.findViewById(R.id.ivInputClose)
        mIvClose.setOnClickListener(this)
//        mBtnTranslate = root.findViewById(R.id.btnTranslate)
        mTvResult = root.findViewById(R.id.tvResult)
//        mBtnTranslate.setOnClickListener(this)
        mIvChangeLanguage = root.findViewById(R.id.ivChange)
        mIvChangeLanguage.setOnClickListener(this)
        mTvSrcLanguage = root.findViewById(R.id.tvSrcLanguage)
        mTvSrcLanguage.text = Lang.AUTO.KEY
        mTvSrcLanguage.setOnClickListener(this)
        mTvTranslateLanguage = root.findViewById(R.id.tvTranslateLanguage)
        mTvTranslateLanguage.text = Lang.ENGLISH.KEY
        mTvTranslateLanguage.setOnClickListener(this)
        mTvTranslateLanguage.text = SettingHelper.getDefaultTranslateLangKey()

        mTvSoundSrcLanguage = root.findViewById(R.id.tvSoundSrcLanguage)
        mTvSrcText = root.findViewById(R.id.tvSrcText)
        mTvSrcSymbol = root.findViewById(R.id.tvSrcSymbol)
        mIvSrcSound = root.findViewById(R.id.ivSoundSrc)
        mIvSrcSound.setOnClickListener(this)
        mIvLiked = root.findViewById(R.id.ivLiked)
        mIvLiked.setOnClickListener(this)
        root.findViewById<View>(R.id.ivCopySrcText).setOnClickListener(this)

        mTvSoundTranslateLanguage = root.findViewById(R.id.tvSoundTranslateLanguage)
        mTvTranslateText = root.findViewById(R.id.tvTranslateText)
        mTvTranslateSymbol = root.findViewById(R.id.tvTranslateSymbol)
        mIvTranslateSound = root.findViewById(R.id.ivSoundTranslate)
        mIvTranslateSound.setOnClickListener(this)
        root.findViewById<View>(R.id.ivCopyTranslateText).setOnClickListener(this)
        root.findViewById<View>(R.id.ivShareTranslateSound).setOnClickListener(this)

        mLlResultContainer = root.findViewById(R.id.llResultContainer)

        mSelectTranslateLangDialog = DialogHelper.createSelectLangDialog(
            activity,
            1,
            listener = object : DialogHelper.SelectLangListener {
                override fun onItemSelected(AlertDialog: AlertDialog?, data: SelectLangItem) {
                    AlertDialog?.dismiss()
                    mTvTranslateLanguage.text = data.lang?.KEY ?: mDefaultLang.KEY
                    SettingHelper.setDefaultTranslateLang(data.lang?.KEY ?: mDefaultLang.KEY)
                    EventBus.getDefault().post(DefaultTranslateLangChangedEvent())
                    translate()
                }
            })
        mSelectSrcLangDialog = DialogHelper.createSelectLangDialog(
            activity,
            0,
            object : DialogHelper.SelectLangListener {
                override fun onItemSelected(AlertDialog: AlertDialog?, data: SelectLangItem) {
                    AlertDialog?.dismiss()
                    mTvSrcLanguage.text = data.lang?.KEY ?: mDefaultLang.KEY
                    if (mEtContent.visibility == View.VISIBLE) {
                        stopRecognize()
                        startRecognize()
                    }
                }
            })

        mEtContent.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val editable = mEtContent.text
                if (editable != null) {
                    val content = editable.toString()
                    hideKeyboard()
                    translate()
                    return@OnEditorActionListener true
                }
            }

            false
        })

        mEtContent.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0) {
                    setVisibility(mIvClose, false)
                    setVisibility(mLlResultContainer, false)
                } else {
                    setVisibility(mIvClose, true)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}


        })

        mIvRecognized = root.findViewById(R.id.ivRecognized)
        mIvRecognized.setOnClickListener(this)

        mCardDictPanel = root.findViewById(R.id.cardDictPanel)

    }

    override fun initData() {


        val argSrcText = arguments?.getString(EXTRA_SRC_TEXT, "") ?: ""
        if (argSrcText.isEmpty()) {
            return
        }
        setVisibility(mInputContainer, false)
        setVisibility(mTvResult, false)
        setVisibility(mIvRecognized, false)
        mEtContent.setText(argSrcText)
        translate()
    }

    override fun createPresenter(): TranslationPresenter = TranslationPresenter()

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
        BaiduVoiceHelper.destroy()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.btnTranslate -> {
//                translate()
//            }
            R.id.ivChange -> {
                //点击只允许中英切换
                if (tvTranslateLanguage.text == Lang.CHINESE.KEY) {
                    mTvTranslateLanguage.text = Lang.ENGLISH.KEY
                } else {
                    mTvTranslateLanguage.text = Lang.CHINESE.KEY
                }
                translate()
            }
            R.id.ivSoundSrc -> {
                val content = mTvSrcText.text.toString()
                val tl = Global.langKeyCodeMap[mTvSoundSrcLanguage.text] ?: mDefaultLang.CODE
                mPresenter.playAudio(content, tl)
            }

            R.id.ivSoundTranslate -> {
                val content = mTvTranslateText.text.toString()
                val tl = Global.langKeyCodeMap[mTvSoundTranslateLanguage.text] ?: mDefaultLang.CODE
                mPresenter.playAudio(content, tl)
            }

            R.id.tvTranslateLanguage -> {
                mSelectTranslateLangDialog?.show()
            }

            R.id.tvSrcLanguage -> {
                mSelectSrcLangDialog?.show()
            }
            R.id.ivInputClose -> {
                mEtContent.setText("")
                setVisibility(mIvClose, false)
            }
            R.id.ivLiked -> {
                liked()
            }
            R.id.ivCopySrcText -> {
                mPresenter?.copyText(mTvSrcText.text?.toString())
            }
            R.id.ivCopyTranslateText -> {
                mPresenter?.copyText(mTvTranslateText.text?.toString())
            }
            R.id.ivShareTranslateSound -> {
                val content = mTvTranslateText.text.toString()
                val tl = Global.langKeyCodeMap[mTvSoundTranslateLanguage.text] ?: mDefaultLang.CODE
                mPresenter.shareAudio(this, content, tl)
            }
            R.id.ivRecognized -> {
                if (mRecognizing) {
                    EventBus.getDefault().post(RecognizedEvent(false))
                    mNeedRestartAudioRecognize = false
                } else {
                    EventBus.getDefault().post(RecognizedEvent(true))
                    mNeedRestartAudioRecognize = true
                }
            }

        }
    }

    override fun onResult(rawText: String, recognizedText: String) {
        if (SettingHelper.getAutoPlayAudio()) {
            stopRecognize()
        }
        mEtContent.setText(recognizedText)
        translate()
    }

    private fun translate() {
        val content = mContent
        val sl = Global.langKeyCodeMap[mTvSrcLanguage.text] ?: mDefaultLang.CODE
        val tl = Global.langKeyCodeMap[mTvTranslateLanguage.text] ?: mDefaultLang.CODE

        mPresenter.translate(content, sl, tl)
    }

    private fun liked() {
        val content = mContent
        val sl = Global.langKeyCodeMap[mTvSrcLanguage.text] ?: mDefaultLang.CODE
        val tl = Global.langKeyCodeMap[mTvTranslateLanguage.text] ?: mDefaultLang.CODE

        mPresenter.liked(content, sl, tl)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLikeUpdate(likeUpdateEvent: LikeUpdateEvent) {
        val history = likeUpdateEvent.history
        val content = mContent
        val sl = Global.langKeyCodeMap[mTvSrcLanguage.text] ?: mDefaultLang.CODE
        val tl = Global.langKeyCodeMap[mTvTranslateLanguage.text] ?: mDefaultLang.CODE
        if (content == history.srcText && sl == history.sl && tl == history.tl) {
            refreshLiked(history.liked == 1)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRemoveLikesEvent(removeLikesEvent: RemoveLikesEvent) {
        removeLikesEvent.list.map {
            val history = it
            val content = mContent
            val sl = Global.langKeyCodeMap[mTvSrcLanguage.text] ?: mDefaultLang.CODE
            val tl = Global.langKeyCodeMap[mTvTranslateLanguage.text] ?: mDefaultLang.CODE
            if (content == history.srcText && sl == history.sl && tl == history.tl) {
                refreshLiked(history.liked == 1)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRecognized(recognizedEvent: RecognizedEvent) {
        mRecognizing = recognizedEvent.recognize
        if (recognizedEvent.recognize) {
            //开启识别
            startRecognize()
        } else {
            //关闭识别
            stopRecognize()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPlayAudioFinish(playAudioFinishEvent: PlayAudioFinishEvent) {
        if (mNeedRestartAudioRecognize) {
            EventBus.getDefault().post(RecognizedEvent(true))
        }
    }

    private fun startRecognize() {
        return
        BaiduVoiceHelper.setRecognizedListener(this)
        mIvRecognized.setImageResource(R.drawable.ic_recognized_stop)
        val type = if (mTvSrcLanguage.text == Lang.ENGLISH.KEY) {
            RecognizedType.ENGLISH
        } else {
            RecognizedType.CHINESE
        }
        BaiduVoiceHelper.startRecognize(type)
    }

    private fun stopRecognize() {
        return
        mIvRecognized.setImageResource(R.drawable.ic_recognized_start)
        BaiduVoiceHelper.stopRecognize()
        BaiduVoiceHelper.removeRecognised(this)
    }

    override fun updateResult(
        data: TranslationBean,
        srcLang: String,
        srcText: String,
        srcSymbol: String,
        translateText: String,
        translateSymbol: String,
        dictText: String
    ) {
        setVisibility(mLlResultContainer, true)

        //发音行显示文本语言
        mTvSoundTranslateLanguage.text = mTvTranslateLanguage.text
        mTvSoundSrcLanguage.text = srcLang
        mTvSrcText.text = srcText
        mTvSrcSymbol.text = srcSymbol
        mTvTranslateText.text = translateText
        mTvTranslateSymbol.text = translateSymbol
        mTvResult.text = dictText
    }

    override fun refreshLiked(liked: Boolean) {
        if (liked) {
            mIvLiked.setImageResource(R.drawable.ic_star_full)
        } else {
            mIvLiked.setImageResource(R.drawable.ic_star_empty)
        }
    }

    override fun showOrHideSoundSrcSymbol(show: Boolean) {
        setVisibility(mTvSrcSymbol, show)
    }

    override fun showOrHideSoundTranslateSymbol(show: Boolean) {
        setVisibility(mTvTranslateSymbol, show)
    }

    override fun showOrHideDictInfo(show: Boolean) {
        setVisibility(mCardDictPanel, show)
    }

    fun hideKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            inputMethodManager.hideSoftInputFromWindow(
                activity?.currentFocus?.windowToken, 0
            )
        }
    }

    companion object {
        const val EXTRA_SRC_TEXT = "EXTRA_SRC_TEXT"
        fun newInstance(srcText: String = ""): TranslationFragment {
            val fragment = TranslationFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_SRC_TEXT, srcText)
            fragment.arguments = bundle
            return fragment
        }
    }
}