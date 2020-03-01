package com.allever.app.translation.text.app

import com.allever.app.translation.text.bean.Lang

object Global {
    var langKeyCodeMap = mutableMapOf<String, String>()
    var langCodeKeyMap = mutableMapOf<String, String>()
    var langList = mutableListOf<Lang>()

    fun initLanguage() {
        langKeyCodeMap[Lang.AUTO.KEY] = Lang.AUTO.CODE
        langKeyCodeMap[Lang.CHINESE.KEY] = Lang.CHINESE.CODE
//        langKeyCodeMap[Lang.CHINESE_CANTONESE.KEY] = Lang.CHINESE_CANTONESE.CODE
        langKeyCodeMap[Lang.CHINESE_TRADITIONAL.KEY] = Lang.CHINESE_TRADITIONAL.CODE
        langKeyCodeMap[Lang.ENGLISH.KEY] = Lang.ENGLISH.CODE
        langKeyCodeMap[Lang.FRENCH.KEY] = Lang.FRENCH.CODE
        langKeyCodeMap[Lang.GERMAN.KEY] = Lang.GERMAN.CODE
        langKeyCodeMap[Lang.JAPANESE.KEY] = Lang.JAPANESE.CODE
        langKeyCodeMap[Lang.RUSSIAN.KEY] = Lang.RUSSIAN.CODE
        langKeyCodeMap[Lang.SPANISH.KEY] = Lang.SPANISH.CODE
        langKeyCodeMap[Lang.ITALIAN.KEY] = Lang.ITALIAN.CODE
        langKeyCodeMap[Lang.THAI.KEY] = Lang.THAI.CODE

        langCodeKeyMap[Lang.AUTO.CODE] = Lang.AUTO.KEY
        langCodeKeyMap[Lang.CHINESE.CODE] = Lang.CHINESE.KEY
//        langCodeKeyMap[Lang.CHINESE_CANTONESE.CODE] = Lang.CHINESE_CANTONESE.KEY
        langCodeKeyMap[Lang.CHINESE_TRADITIONAL.CODE] = Lang.CHINESE_TRADITIONAL.KEY
        langCodeKeyMap[Lang.ENGLISH.CODE] = Lang.ENGLISH.KEY
        langCodeKeyMap[Lang.FRENCH.CODE] = Lang.FRENCH.KEY
        langCodeKeyMap[Lang.GERMAN.CODE] = Lang.GERMAN.KEY
        langCodeKeyMap[Lang.JAPANESE.CODE] = Lang.JAPANESE.KEY
        langCodeKeyMap[Lang.RUSSIAN.CODE] = Lang.RUSSIAN.KEY
        langCodeKeyMap[Lang.SPANISH.CODE] = Lang.SPANISH.KEY
        langCodeKeyMap[Lang.ITALIAN.CODE] = Lang.ITALIAN.KEY
        langCodeKeyMap[Lang.THAI.CODE] = Lang.THAI.KEY

        langList.add(Lang.AUTO)
        langList.add(Lang.CHINESE)
//        langList.add(Lang.CHINESE_CANTONESE)
        langList.add(Lang.CHINESE_TRADITIONAL)
        langList.add(Lang.ENGLISH)
        langList.add(Lang.FRENCH)
        langList.add(Lang.GERMAN)
        langList.add(Lang.JAPANESE)
        langList.add(Lang.RUSSIAN)
        langList.add(Lang.SPANISH)
        langList.add(Lang.ITALIAN)
        langList.add(Lang.THAI)

    }
}