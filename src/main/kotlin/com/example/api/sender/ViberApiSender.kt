package com.example.api.sender

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class ViberApiSender {
    private val client = HttpClient(CIO)
    private val AUTH_HEADER_NAME = "X-Viber-Auth-Token"

    suspend fun sendMessage(messageBody: String): HttpResponse {
        val client1 = HttpClient(CIO)

        val response: HttpResponse = client1.post("https://chatapi.viber.com/pa/send_message") {
            headers {
                append(AUTH_HEADER_NAME, getAuthToken())
            }
            body = messageBody
        }
        client1.close()

        return response
    }

    suspend fun registerBotWebhook(): HttpResponse {
        val response: HttpResponse = client.post("https://chatapi.viber.com/pa/set_webhook") {
            headers {
                append(AUTH_HEADER_NAME, getAuthToken())
            }
            body = """
            {
               "url":"https://viber-bot-ua-help.herokuapp.com/webhook",
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

    private fun getAuthToken(): String {
        return System.getenv("VIBER_TOKEN") ?: throw RuntimeException("VIBER_TOKEN env variable it not specified")
    }
}

