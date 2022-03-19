package com.example.api.sender

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import mu.KotlinLogging


class ViberApiSender(private val serverHostname: String) {
    private val logger = KotlinLogging.logger { }
    private val client = HttpClient(CIO)
    private val authHeaderName = "X-Viber-Auth-Token"

    suspend fun sendMessage(messageBody: String): HttpResponse {
        val response: HttpResponse = client.post("https://chatapi.viber.com/pa/send_message") {
            headers {
                append(authHeaderName, getAuthToken())
            }
            body = messageBody
        }
        val responseBody: String = response.receive()
        logger.trace { "Send message response:$response -> $responseBody" }
        return response
    }

    suspend fun registerBotWebhook(): HttpResponse {
        val response: HttpResponse = client.post("https://chatapi.viber.com/pa/set_webhook") {
            headers {
                append(authHeaderName, getAuthToken())
            }
            body = """
            {
               "url":"$serverHostname/webhook",
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

