package com.example

import com.beust.klaxon.Klaxon
import com.example.api.model.ConversationStartedEvent
import com.example.api.model.Event
import io.ktor.application.*
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    val klaxon = Klaxon()

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        get("/about") {
            call.respondText("Viber bot for psychological help")
        }
        get("/register_webhook") {
            val response = registerBotWebhook()
            val stringBody: String = response.receive()
            call.respondText(response.toString() + "\n" + stringBody)
        }

        post("/webhook") {
            val body = call.receiveText()
            logger.info { "Webhook received: $call $body" }

            val event = Event.fromJson(body)
            when (event.event) {
                "conversation_started" -> handleConversationStarted(klaxon.parse<ConversationStartedEvent>(body)!!)
                //todo handle all events
                else -> logger.warn { "Unhandled event: ${event.event}" }
            }

            call.respond(HttpStatusCode.OK)
        }
    }
}

fun handleConversationStarted(event: ConversationStartedEvent) {
    logger.info { "Handling:$event" }
    //TODO("Implement")
}
