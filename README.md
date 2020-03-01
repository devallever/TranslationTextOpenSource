# TextTranslatorOpenSource-文本翻译器开源版

> 仅用于学习研究之用，请勿商用

## 介绍

【文本翻译器】是一款免费的简洁实用的翻译软件。文本翻译器应用程序完全免费，可以非常快速翻译您的单词，帮助您与外国人交流。文本翻译器适用于旅行者、学生、商人和其他语言爱好者，使用文本翻译器可以轻松了解其他语言。文本翻译器支持多国语言，全新领先的翻译引擎，让各种变得更加可靠有保证。界面设计简洁、优雅，体积小巧，但是功能很强大哦。赶快下载来试试吧~


## 功能特点：  
*【词典解析】除了基本的翻译外，提供更详细的词典功能，词性分类  
*【多语言】目前支持主流语言：中文，中文繁体，英语，日语，法语，德语  
*【单词本】收藏喜欢的单词  
*【历史记录】记录翻译记录  
*【离线翻译】如果已经翻译过的即使没有网络也能翻译   
*【数据备份和恢复】备份历史翻译记录和恢复记录  
*【自动朗读】翻译后为您朗读  
*【自动复制】将翻译文本自动复制到剪贴板  
*【全局复制查词】在任何界面点复制就能查单词  
*【免费】使用功能过程中完全免费  
*【界面简洁】界面设计优雅、简洁  


## 展示

|||||
|:-:|:-:|:-:|:-:|
|![](https://upload-images.jianshu.io/upload_images/2359130-8857f14c31f9465e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](https://upload-images.jianshu.io/upload_images/2359130-c3fefa9bc4a4ad77.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](https://upload-images.jianshu.io/upload_images/2359130-4e74efe4498c6d57.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](https://upload-images.jianshu.io/upload_images/2359130-fa6b1639a25a32ab.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|
|![](https://upload-images.jianshu.io/upload_images/2359130-b1663262b0845cf2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](https://upload-images.jianshu.io/upload_images/2359130-107224988e90a7d7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](https://upload-images.jianshu.io/upload_images/2359130-ce99e7699ef3e849.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
||

> [如果无法显示图片，请移步到这里](https://www.jianshu.com/p/635dbd2660f3)


## 下载

### Google Play

![](https://upload-images.jianshu.io/upload_images/2359130-ca75e4bebe10f2a7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/200)


[https://play.google.com/store/apps/details?id=com.allever.app.translation.text](https://play.google.com/store/apps/details?id=com.allever.app.translation.text)


### Baidu

[https://shouji.baidu.com/software/26838949.html](https://shouji.baidu.com/software/26838949.html)

## 项目地址
[https://github.com/devallever/TranslationTextOpenSource](https://github.com/devallever/TranslationTextOpenSource)



## 项目架构

项目采用多组件 + MVP 架构

### 项目组件架构图

![](https://upload-images.jianshu.io/upload_images/2359130-9bf2b5dcd6e97caf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- common：通用模块，包括基类和通用工具类，基本上所有模块都依赖此模块
- commont：评分模块
- permission：申请权限模块
- recomend：推广模块
- umeng：友盟统计
- widget：通用UI组件模块

### 项目包图

![](https://upload-images.jianshu.io/upload_images/2359130-7280011e5d1f31a7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- app：基类和全局类
- bean：实体类和EventBus事件类
- function：功能类
- ui：界面，包括mvp
- util：工具类

其中主要代码是在ui和function这两个包

![](https://upload-images.jianshu.io/upload_images/2359130-84947802cce5f6d4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


## 如何使用

- 项目需要依赖AndroidDependencyLib中的一个或多个模块，请预先配置
> [https://github.com/devallever/AndroidDependencyLib](https://github.com/devallever/AndroidDependencyLib)

- 把AndroidDependencyLib项目和本项目放同一个目录下

- 项目需要依赖AndroidUIKit中的一个或多个模块，请预先配置
> [https://github.com/devallever/AndroidUIKit](https://github.com/devallever/AndroidUIKit)


## 接口

> [https://translate.google.cn/](https://translate.google.cn/)

### 翻译接口

> [https://translate.google.cn/translate_a/single?client=gtx&dt=t&dt=bd&dt=rm&dj=1&ie=UTF-8&oe=UTF-8&sl=auto&tl=zh-CN&hl=zh-CN&tk=&q=cat](https://translate.google.cn/translate_a/single?client=gtx&dt=t&dt=bd&dt=rm&dj=1&ie=UTF-8&oe=UTF-8&sl=auto&tl=zh-CN&hl=zh-CN&tk=&q=cat)

可以参考以下默认值
```
    @GET("translate_a/single")
    fun translate(
        @Query("q") q: String,
        @Query("client") content: String = "gtx",
        @Query("dt") dt: String = "t",
        @Query("dt") dt1: String = "bd",
        @Query("dt") dt2: String = "rm",
        @Query("dj") dj: String = "1",
        @Query("ie") ie: String = "UTF-8",
        @Query("oe") oe: String = "UTF-8",
        @Query("sl") sl: String = "auto",
        @Query("tl") tl: String = "en",
        @Query("hl") hl: String = "zh-CN",
        @Query("tk") tk: String = ""

    ): Observable<TranslationBean>
```

主要用到sl、tl和q这几个参数
- sl：原语种
- tl：翻译语种
- q：翻译文本

关于语种可以参考项目中Languages这个类，包含了100多个语种代码

- Languages

返回json对应TranslationBean.kt这个实体类

```
/***
 * {"sentences":[{"trans":"串","orig":"string","backend":2},{"translit":"Chuàn","src_translit":"striNG"}],"dict":[{"pos":"名词","terms":["串","弦","线","绳","绳子","细线","鞭","绲"],"entry":[{"word":"串","reverse_translation":["string"],"score":0.13323711},{"word":"弦","reverse_translation":["string","chord","bowstring","hypotenuse","subtense","string of musical instrument"],"score":0.016418032},{"word":"线","reverse_translation":["line","wire","thread","string","route","filament"],"score":0.0058540297},{"word":"绳","reverse_translation":["rope","cord","string"],"score":0.00477792},{"word":"绳子","reverse_translation":["rope","string","cord"],"score":0.0023652418},{"word":"细线","reverse_translation":["thread","string"],"score":2.2698537E-4},{"word":"鞭","reverse_translation":["whip","lash","string","firecracker","iron staff used as a weapon"],"score":8.139759E-6},{"word":"绲","reverse_translation":["cord","embroidered sash","string"],"score":2.4439987E-6}],"base_form":"string","pos_enum":1},{"pos":"动词","terms":["纫"],"entry":[{"word":"纫","reverse_translation":["thread","string"],"score":4.860472E-6}],"base_form":"string","pos_enum":2}],"src":"en","confidence":0.9488189,"ld_result":{"srclangs":["en"],"srclangs_confidences":[0.9488189],"extended_srclangs":["en"]}}
 */
@Keep
class TranslationBean {
    /**
     * sentences : [{"trans":"串","orig":"string","backend":2},{"translit":"Chuàn","src_translit":"striNG"}]
     * dict : [{"pos":"名词","terms":["串","弦","线","绳","绳子","细线","鞭","绲"],"entry":[{"word":"串","reverse_translation":["string"],"score":0.13323711},{"word":"弦","reverse_translation":["string","chord","bowstring","hypotenuse","subtense","string of musical instrument"],"score":0.016418032},{"word":"线","reverse_translation":["line","wire","thread","string","route","filament"],"score":0.0058540297},{"word":"绳","reverse_translation":["rope","cord","string"],"score":0.00477792},{"word":"绳子","reverse_translation":["rope","string","cord"],"score":0.0023652418},{"word":"细线","reverse_translation":["thread","string"],"score":2.2698537E-4},{"word":"鞭","reverse_translation":["whip","lash","string","firecracker","iron staff used as a weapon"],"score":8.139759E-6},{"word":"绲","reverse_translation":["cord","embroidered sash","string"],"score":2.4439987E-6}],"base_form":"string","pos_enum":1},{"pos":"动词","terms":["纫"],"entry":[{"word":"纫","reverse_translation":["thread","string"],"score":4.860472E-6}],"base_form":"string","pos_enum":2}]
     * src : en
     * confidence : 0.9488189
     * ld_result : {"srclangs":["en"],"srclangs_confidences":[0.9488189],"extended_srclangs":["en"]}
     */

    var src: String? = null
    var confidence: Double = 0.toDouble()
    var ld_result: LdResultBean? = null
    var sentences: List<SentencesBean>? = null
    var dict: List<DictBean>? = null

    @Keep
    class LdResultBean {
        var srclangs: List<String>? = null
        var srclangs_confidences: List<Double>? = null
        var extended_srclangs: List<String>? = null
    }

    @Keep
    class SentencesBean {
        /**
         * trans : 串
         * orig : string
         * backend : 2
         * translit : Chuàn
         * src_translit : striNG
         */

        var trans: String? = null
        var orig: String? = null
        var backend: Int = 0
        var translit: String? = null
        var src_translit: String? = null
    }

    @Keep
    class DictBean {
        /**
         * pos : 名词
         * terms : ["串","弦","线","绳","绳子","细线","鞭","绲"]
         * entry : [{"word":"串","reverse_translation":["string"],"score":0.13323711},{"word":"弦","reverse_translation":["string","chord","bowstring","hypotenuse","subtense","string of musical instrument"],"score":0.016418032},{"word":"线","reverse_translation":["line","wire","thread","string","route","filament"],"score":0.0058540297},{"word":"绳","reverse_translation":["rope","cord","string"],"score":0.00477792},{"word":"绳子","reverse_translation":["rope","string","cord"],"score":0.0023652418},{"word":"细线","reverse_translation":["thread","string"],"score":2.2698537E-4},{"word":"鞭","reverse_translation":["whip","lash","string","firecracker","iron staff used as a weapon"],"score":8.139759E-6},{"word":"绲","reverse_translation":["cord","embroidered sash","string"],"score":2.4439987E-6}]
         * base_form : string
         * pos_enum : 1
         */

        var pos: String? = null
        var base_form: String? = null
        var pos_enum: Int = 0
        var terms: List<String>? = null
        var entry: List<EntryBean>? = null

        @Keep
        class EntryBean {
            /**
             * word : 串
             * reverse_translation : ["string"]
             * score : 0.13323711
             */

            var word: String? = null
            var score: Double = 0.toDouble()
            var reverse_translation: List<String>? = null
        }
    }
}

```

- sentences 字段含原文本和翻译文本
- dict 字段包含词典信息
- 解析获取对应字段的内容在 TranslationHelper 中


### 语音接口

> [https://translate.google.cn/translate_tts?client=gtx&ie=UTF-8&tl=zh-CN&total=1&idx=0&textlen=2&tk=&q=setting](https://translate.google.cn/translate_tts?client=gtx&ie=UTF-8&tl=zh-CN&total=1&idx=0&textlen=2&tk=&q=setting)

其中主要用到 tl 和 q 参数，同上

可以参考以下默认值

```
    @GET("translate_tts")
    fun requestTTS(
        @Query("q") q: String,
        @Query("client") content: String = "gtx",
        @Query("ie") ie: String = "UTF-8",
        @Query("tl") tl: String = "en",
        @Query("hl") hl: String = "zh-CN",
        @Query("total") total: String = "1",
        @Query("idx") idx: String = "0",
        @Query("textlen") textlen: String = "0",
        @Query("tk") tk: String = ""
    ): Call<ResponseBody>
```

## 翻译基本流程

项目采用MVP架构

 - TranslationFragment：调用presenter接口进行请求翻译
```
mPresenter.translate(content, sl, tl)
```
 - TranslationPresenter：调用RetrofitUtil进行网络请求，并回调TranslationView的接口刷新界面
 - DBHelper：负责存取翻译记录

 ```
    fun translate(content: String, sl: String = Lang.AUTO.CODE, translateLanguage: String) {
        if (content.isEmpty()) {
            toast(R.string.please_input_content)
            return
        }

        val history = DBHelper.getHistory(content, sl, translateLanguage)
        val translationBean =
            JsonHelper.json2Object(history?.result ?: "", TranslationBean::class.java)
        if (translationBean != null) {
            parse(translationBean)
            mViewRef?.get()?.refreshLiked(history?.liked == 1)
            val translateText = TranslationHelper.getTranslateText(translationBean)
            if (translateText.isNotEmpty()) {
                play(translateText, translateLanguage)
                copyToClipBoard(translateText)
            }
            log("获取到数据库翻译内容")
            DBHelper.updateHistoryTime(history)
            EventBus.getDefault().post(UpdateRecordEvent())
            return
        }

        RetrofitUtil.translate(object : Subscriber<TranslationBean>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
                log("失败")
            }

            override fun onNext(bean: TranslationBean) {
                parse(bean)
                val translateText = TranslationHelper.getTranslateText(bean)

                mViewRef?.get()?.refreshLiked(false)

                play(translateText, translateLanguage)

                copyToClipBoard(translateText)

                DBHelper.addHistory(content, sl, translateLanguage, bean)

                EventBus.getDefault().post(UpdateRecordEvent())

            }
        }, content, sl, translateLanguage)
    }
```

 - TranslationHelper：负责解析数据
 - TranslationView：刷新界面
```
    private fun parse(bean: TranslationBean) {

        val srcSymbol = TranslationHelper.getSrcSymbol(bean)
        val translateSymbol = TranslationHelper.getTranslateSymbol(bean)
        //音标显示逻辑
        if (srcSymbol.isNotEmpty()) {
            mViewRef?.get()?.showOrHideSoundSrcSymbol(true)
        } else {
            mViewRef?.get()?.showOrHideSoundSrcSymbol(false)
        }
        if (translateSymbol.isNotEmpty()) {
            mViewRef?.get()?.showOrHideSoundTranslateSymbol(true)
        } else {
            mViewRef?.get()?.showOrHideSoundTranslateSymbol(false)
        }

        if (TranslationHelper.getDictText(bean).isNotEmpty()) {
            mViewRef?.get()?.showOrHideDictInfo(true)
        } else {
            mViewRef?.get()?.showOrHideDictInfo(false)
        }

        mViewRef?.get()?.updateResult(
            bean,
            TranslationHelper.getSrcLang(bean),
            TranslationHelper.getSrcText(bean),
            TranslationHelper.getSrcSymbol(bean),
            TranslationHelper.getTranslateText(bean),
            TranslationHelper.getTranslateSymbol(bean),
            TranslationHelper.getDictText(bean)
        )
    }
```

以上就是翻译的基本流程

## 复制查词功能实现

- 监听粘贴板变化并弹出Dialog风格的Activity，当在应用内复制时候，不弹出。

```
        val clipBoardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipBoardManager.addPrimaryClipChangedListener {
            log("剪贴板变化")
            if (ActivityCollector.size() == 0 && SettingHelper.getAutoTranslate()) {
                val srcText = clipBoardManager.primaryClip?.getItemAt(0)?.text.toString()
                DialogTranslateActivity.start(this, srcText)
            }
        }
```

## 通知栏常驻

- 启动一个前台服务 TranslationService 保活

```
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("启动服务")
        val paddingFlag = 1
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainDrawerActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT,
            null
        )
        val notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "translation"
            val channelName = "翻译"
            val importance = NotificationManager.IMPORTANCE_HIGH
            createNotificationChannel(channelId, channelName, importance)
            NotificationCompat.Builder(this, channelId)
                .setNumber(paddingFlag)
        } else {
            NotificationCompat.Builder(this)
        }
        notificationBuilder
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_msg, getString(R.string.app_name)))
            .setSmallIcon(R.drawable.ic_logo)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_logo))
            .setContentIntent(pendingIntent)
        startForeground(1, notificationBuilder.build())
        return super.onStartCommand(intent, flags, startId)
    }
```

## 离线翻译功能

将翻译过的记录保存下来，即每次请求网络翻译时候就保存记录

```
//sl：原语种代码
//translateLanguage：翻译语种代码
//bean：TranslationBean请求翻译的实体类
DBHelper.addHistory(content, sl, translateLanguage, bean)
```
通过History这个实体类保存翻译记录

```
    fun addHistory(content: String, sl: String, tl: String, bean: TranslationBean) {
        run {
            try {
                val history = History()
                history.srcText = content
                history.sl = sl
                history.tl = tl
                history.time = System.currentTimeMillis()
                history.liked = 0
                history.result = Gson().toJson(bean)
                history.ttsPath = MD5.getMD5StrToLowerCase("$content$tl.mp3")
                val saveResult = history.save()
                if (saveResult) {
                    log("保存翻译成功")
                } else {
                    loge("保存翻译失败")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                loge("保存翻译失败")
            }
        }
    }
```
History实体类

```
@Keep
class History : LitePalSupport() {
    var srcText: String = ""
    var sl: String = "en"
    var tl: String = "en"
    var time: Long = 0
    var liked: Int = 0
    //全路径
    var ttsPath: String = ""
    var result: String = ""
}
```

每次请求就先获取本地记录

```
        val history = DBHelper.getHistory(content, sl, translateLanguage)
        val translationBean =
            JsonHelper.json2Object(history?.result ?: "", TranslationBean::class.java)
        if (translationBean != null) {
            parse(translationBean)
            mViewRef?.get()?.refreshLiked(history?.liked == 1)
            val translateText = TranslationHelper.getTranslateText(translationBean)
            if (translateText.isNotEmpty()) {
                play(translateText, translateLanguage)
                copyToClipBoard(translateText)
            }
            log("获取到数据库翻译内容")
            DBHelper.updateHistoryTime(history)
            EventBus.getDefault().post(UpdateRecordEvent())
            return
        }
```

## 历史记录和单词本

- 单词本从数据库中按条件查找like = 1 的记录，，四种情况就是选中所要查找的语言
```
    fun getLikedHistory(sl: String, tl: String) {
        run {
            try {
                //四种情况
                var historyList = mutableListOf<History>()
                if (sl.isEmpty() && tl.isEmpty()) {
                    //查所有
                    historyList =
                        LitePal.where("liked = ?", "1").order("time desc").find(History::class.java)
                }

                if (sl.isNotEmpty() && tl.isNotEmpty()) {
                    historyList = LitePal
                        .where("sl = ? and tl = ? and liked = ?", sl, tl, "1")
                        .order("time desc")
                        .find(History::class.java)
                }

                if (sl.isEmpty() && tl.isNotEmpty()) {
                    historyList = LitePal
                        .where("tl = ? and liked = ?", tl, "1")
                        .order("time desc")
                        .find(History::class.java)
                }

                if (sl.isNotEmpty() && tl.isEmpty()) {
                    historyList = LitePal
                        .where("sl = ? and liked = ?", sl, "1")
                        .order("time desc")
                        .find(History::class.java)
                }
                val wordItemList = mutableListOf<WordItem>()
                historyList.map {
                    val wordItem = WordItem()
                    wordItem.history = it
                    wordItem.checked = false
                    wordItemList.add(wordItem)
                }
                mViewRef?.get()?.updateWordList(wordItemList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
```

- 历史记录就是全部记录，四种情况就是选中所要查找的语言

```
    fun getHistory(sl: String, tl: String) {
        run {
            try {
                //四种情况
                var historyList = mutableListOf<History>()
                if (sl.isEmpty() && tl.isEmpty()) {
                    //查所有
                    historyList = (LitePal.order("time desc").find(History::class.java))
                }

                if (sl.isNotEmpty() && tl.isNotEmpty()) {
                    historyList = LitePal
                        .where("sl = ? and tl = ?", sl, tl)
                        .order("time desc")
                        .find(History::class.java)
                }

                if (sl.isEmpty() && tl.isNotEmpty()) {
                    historyList = LitePal
                        .where("tl = ?", tl)
                        .order("time desc")
                        .find(History::class.java)
                }

                if (sl.isNotEmpty() && tl.isEmpty()) {
                    historyList = LitePal
                        .where("sl = ?", sl)
                        .order("time desc")
                        .find(History::class.java)
                }
                val wordItemList = mutableListOf<WordItem>()
                historyList.map {
                    val wordItem = WordItem()
                    wordItem.history = it
                    wordItem.checked = false
                    wordItemList.add(wordItem)
                }
                mViewRef?.get()?.updateWordList(wordItemList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
```


## 数据备份和恢复功能

将所有History记录取出来，封装成BackupBean，再转成json保存到文件中

```
val historyList = DBHelper.getAllHistory()
if (historyList.isEmpty()) {
  toast(R.string.no_backup_data)
  task.run()
  return
}
val backupBean = BackupBean()
backupBean.data = historyList
val result = Gson().toJson(backupBean)
log("backupResult = $result")
val success = FileUtil.saveStringToFile(result, BACKUP_FILE_PATH)
if (success) {
  toast(R.string.backup_success)
} else {
  toast(R.string.backup_fail)
}
...
```

## 最后一个是百度的语音识别功能

项目中集成了百度语音识别，但控件被隐藏，逻辑还是有的，通过一个ImageView触发。
识别功能封装在baiduvoice模块中的BaiduVoiceHelper
```
object BaiduVoiceHelper : EventListener {

    private var mEventManager: EventManager? = null
    private var mRecognizedListenerList = mutableListOf<RecognizedListener>()
    private var mRecognizedType: RecognizedType = RecognizedType.CHINESE
//    private var mListener: RecognizedListener? = null

    //重新识别
    private var mNeedReRecognized = true

    fun init() {
        mEventManager = EventManagerFactory.create(App.context, "asr")
        mEventManager?.registerListener(this)
    }

    fun setRecognizedListener(recognizedListener: RecognizedListener) {
        mRecognizedListenerList.add(recognizedListener)
    }

    fun removeRecognised(recognizedListener: RecognizedListener) {
        mRecognizedListenerList.remove(recognizedListener)
    }

    fun startRecognize(type: RecognizedType = mRecognizedType) {
        mNeedReRecognized = true
        mRecognizedType = type
        val params = LinkedHashMap<String, Any>()
        var event: String? = null
        event = SpeechConstant.ASR_START // 替换成测试的event

        // 基于SDK集成2.1 设置识别参数
        params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        // params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        if (mRecognizedType == RecognizedType.ENGLISH) {
            params[SpeechConstant.PID] = 1737 //英语
        } else {
            params[SpeechConstant.PID] = 15362 //普通话搜索模型, 默认
        }


        /* 语音自训练平台特有参数 */
        // params.put(SpeechConstant.PID, 8002);
        // 语音自训练平台特殊pid，8002：搜索模型类似开放平台 1537  具体是8001还是8002，看自训练平台页面上的显示
        // params.put(SpeechConstant.LMID,1068); // 语音自训练平台已上线的模型ID，https://ai.baidu.com/smartasr/model
        // 注意模型ID必须在你的appId所在的百度账号下
        /* 语音自训练平台特有参数 */

        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        AutoCheck(App.context, @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 100) {
                    val autoCheck = msg.obj as AutoCheck
                    synchronized(autoCheck) {
                        val message =
                            autoCheck.obtainErrorMessage() // autoCheck.obtainAllMessage();
//                        txtLog.append(message + "\n")
                        log(message)
                        // Log.w("AutoCheckMessage", message);
                    }// 可以用下面一行替代，在logcat中查看代码
                }
            }
        }, false).checkAsr(params)
        var json: String? = null // 可以替换成自己的json
        json = JSONObject(params as Map<*, *>).toString() // 这里可以替换成你需要测试的json
        mEventManager?.send(event, json, null, 0, 0)
        log("开始识别: 输入参数：$json")
    }

    fun stopRecognize() {
        mNeedReRecognized = false
        log("停止识别：ASR_STOP")
        mEventManager?.send(SpeechConstant.ASR_STOP, null, null, 0, 0) //
    }

    fun destroy() {
        stopRecognize()
        mEventManager?.unregisterListener(this)
    }

    // 基于sdk集成1.2 自定义输出事件类 EventListener 回调方法
    // 基于SDK集成3.1 开始回调事件
    override fun onEvent(
        name: String,
        params: String?,
        data: ByteArray?,
        offset: Int,
        length: Int
    ) {
        var logTxt = "name: $name"


        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :$params"
        }
        if (data != null) {
            logTxt += " ;data length=" + data.size
        }
        log(logTxt)

        if (params?.contains("final_result") == true) {
            loge("最后识别结果: $logTxt")
            try {
                val jsonObject = JSONObject(params)
                val result = jsonObject.getString("best_result")?:""
                mRecognizedListenerList.map {
                    it.onResult(params, result)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (name == "asr.exit" && mNeedReRecognized) {
            startRecognize()
            //重新识别
        }
    }
}
```

## 用到的开源项目
 - Eventbus
 - LitePal: 数据库
 - RxJava/RxAndroid
 - Retrofit/Okhttp
 - Glide

## 更多项目

> [VirtualCallOpenSource-虚拟来电开源版]([https://github.com/devallever/VirtualCallOpenSource](https://github.com/devallever/VirtualCallOpenSource)
)

> [LoseWeight-减肥健身App开源版](https://github.com/devallever/LoseWeightOpenSOurce)


## 最后
如果喜欢，请star
最重要一点，请勿商用。
如非必要，请更改包名，类名，包结构。
谢谢。

我的Github
> [[https://github.com/devallever](https://github.com/devallever)
]([https://github.com/devallever](https://github.com/devallever)
)
 
 
 
