package com.example.translator.core.domain.util

import kotlinx.coroutines.DisposableHandle

fun interface DisposableHandle : DisposableHandle

// it is equivalent to the following code:

/*
fun DisposableHandle(block: () -> Unit): DisposableHandle {
    return object : DisposableHandle {
        override fun dispose() {
            block()
        }
    }
}
*/


