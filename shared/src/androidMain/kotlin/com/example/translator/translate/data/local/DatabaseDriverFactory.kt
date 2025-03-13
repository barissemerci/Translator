package com.example.translator.translate.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.translator.database.TranslateDatabase

actual class DatabaseDriverFactory(
    private val context: Context
){
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(
            schema = TranslateDatabase.Schema,
            context = context,
            name = "translate.db"
        )
    }
}