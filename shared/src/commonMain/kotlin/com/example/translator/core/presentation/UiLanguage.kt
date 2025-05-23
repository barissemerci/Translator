package com.example.translator.core.presentation

import com.example.translator.core.domain.language.Language

expect class UiLanguage {
    val language : Language
    companion object {
       fun byCode(langCode: String): UiLanguage
       val allLanguages : List<UiLanguage>
    }
}