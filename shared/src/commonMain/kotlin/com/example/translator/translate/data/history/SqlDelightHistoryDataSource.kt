package com.example.translator.translate.data.history

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.translator.core.domain.util.CommonFlow
import com.example.translator.core.domain.util.toCommonFlow
import com.example.translator.database.TranslateDatabase
import com.example.translator.translate.domain.history.HistoryDataSource
import com.example.translator.translate.domain.history.HistoryItem
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlin.coroutines.CoroutineContext

class SqlDelightHistoryDataSource(
    db : TranslateDatabase
) : HistoryDataSource {
    private val queries = db.translateQueries
    override fun getHistory(context: CoroutineContext): CommonFlow<List<HistoryItem>> {
        return queries.getHistory().asFlow().mapToList(context).map { history ->
            history.map { it.toHistoryItem() }
           }.toCommonFlow()
        }


    override suspend fun insertHistoryItem(item: HistoryItem) {
       queries.insertHistoryEntity(
           id = item.id,
           fromLanguageCode = item.fromLanguageCode,
           fromText = item.fromText,
           toLanguageCode = item.toLanguageCode,
           toText = item.toText,
           timestamp = Clock.System.now().toEpochMilliseconds()
       )
    }
}