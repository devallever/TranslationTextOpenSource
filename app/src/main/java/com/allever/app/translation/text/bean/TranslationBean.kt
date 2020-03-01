package com.allever.app.translation.text.bean

import androidx.annotation.Keep


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
