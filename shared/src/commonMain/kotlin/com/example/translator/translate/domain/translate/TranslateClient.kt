package com.example.translator.translate.domain.translate

import com.example.translator.core.domain.language.Language

interface TranslateClient {
    suspend fun translate(
        fromLanguage: Language,
        text: String,
        toLanguage: Language
    ): String
}