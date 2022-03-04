package com.example.api.sender

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ViberApiSender {
    private val client = HttpClient(CIO)

    suspend fun sendMessage(): HttpResponse {
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

        return response
    }


    //todo call shutdown on stop server
    fun shutdown() {
        client.close()
    }
}