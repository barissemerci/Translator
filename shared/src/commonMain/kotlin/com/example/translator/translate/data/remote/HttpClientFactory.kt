package com.example.translator.translate.data.remote

import io.ktor.client.HttpClient

expect class HttpClientFactory {
     fun create() : HttpClient
}