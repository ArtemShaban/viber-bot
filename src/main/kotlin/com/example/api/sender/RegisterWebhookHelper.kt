package com.example

import com.example.api.sender.getAutHeaderName
import com.example.api.sender.getAuthToken
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun registerBotWebhook(): HttpResponse {
    val client = HttpClient(CIO)

    val response: HttpResponse = client.post("https://chatapi.viber.com/pa/set_webhook") {
        headers {
            append(getAutHeaderName(), getAuthToken())
            append(HttpHeaders.Accept, "text/html")
            append(HttpHeaders.Authorization, "token")
            append(HttpHeaders.UserAgent, "ktor client")
        }
        body = """
            {
               "url":"https://viber-bot-ua.herokuapp.com/webhook",
               "send_name": true,
               "send_photo": true
            }
            """
    }

    client.close()
    return response
}