package com.example.translator.di

import com.example.translator.translate.data.history.SqlDelightHistoryDataSource
import com.example.translator.translate.domain.history.HistoryDataSource
import com.example.translator.translate.domain.translate.Translate
import com.example.translator.translate.domain.translate.TranslateClient
import com.example.translator.translate.data.translate.KtorTranslateClient
import com.example.translator.translate.data.local.DatabaseDriverFactory
import com.example.translator.database.TranslateDatabase
import com.example.translator.translate.data.remote.HttpClientFactory
class AppModule {
    val historyDataSource: HistoryDataSource by lazy {
        SqlDelightHistoryDataSource(
            TranslateDatabase(
                DatabaseDriverFactory().create()
            )
        )
    }

    private val translateClient: TranslateClient by lazy {
        KtorTranslateClient(
            HttpClientFactory().create()
        )
    }

    val translateUseCase: Translate by lazy {
        Translate(translateClient, historyDataSource)
    }
}