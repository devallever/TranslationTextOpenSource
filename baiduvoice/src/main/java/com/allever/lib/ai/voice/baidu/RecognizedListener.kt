package com.allever.lib.ai.voice.baidu

interface RecognizedListener {
    fun onResult(rawText: String, recognizedText: String)
}