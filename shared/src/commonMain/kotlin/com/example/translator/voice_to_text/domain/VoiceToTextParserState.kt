package com.example.translator.voice_to_text.domain

data class VoiceToTextParserState(
    val result : String = "",
    val error : String?  = null,
    val powerRatio : Float = 0f,
    val isSpeaking : Boolean = false
)
