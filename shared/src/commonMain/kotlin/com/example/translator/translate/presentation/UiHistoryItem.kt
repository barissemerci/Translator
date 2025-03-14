package com.example.translator.translate.presentation

import com.example.translator.core.presentation.UiLanguage

data class UiHistoryItem(
    val id: Long,
    val fromLanguage: UiLanguage,
    val fromText: String,
    val toLanguage: UiLanguage,
    val toText: String
)