package com.example.translator.core.domain.util

import kotlinx.coroutines.flow.MutableStateFlow

actual class CommonMutableStateFlow<T> actual constructor(
    private val flow : MutableStateFlow<T>
) : MutableStateFlow<T> by flow