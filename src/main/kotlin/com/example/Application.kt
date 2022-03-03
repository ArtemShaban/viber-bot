package com.example

import com.beust.klaxon.Klaxon
import com.example.api.model.*
import com.example.logic.BotLogic
import com.example.logic.BotLogicState
import com.example.logic.request.UserRequest
import io.ktor.application.*
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import mu.KotlinLogging
import java.io.StringReader

private val logger = KotlinLogging.logger { }
private val klaxon = Klaxon()

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {

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

            val parsed = klaxon.parseJsonObject(StringReader(body))
            val response: String
            when (val event = parsed["event"]) {
                "conversation_started" -> {
                    response = handleConversationStarted(klaxon.parse<ConversationStartedEvent>(body)!!)
                }
                //todo handle all events
                else -> {
                    response = ""
                    logger.warn { "Unhandled event: $event" }
                }
            }
            logger.debug { "Sending response: $response" }
            call.respond(HttpStatusCode.OK, response)
        }
    }
}

fun handleConversationStartedPrev(event: ConversationStartedEvent): String {
    val welcomeMessage = WelcomeMessage(
        Sender("Чат бот"),
        "text",
        "Доброго дня!" +
                "\nДякуємо, що звернулися до нашої служби #психологічної підтримки!" +
                "\nНаші спеціалісти готові вам надати допомогу в зручному для вас форматі, який допоможе визначити Я чат-бот." +
                "\n\nВибери мову спілкування" +
                "\nChoose language",
        "choose_lang_stage",
        Keyboard(
            type = "keyboard",
            defaultHeight = false,
            inputFieldState = "hidden",
            listOf(
                Button(
                    actionType = "reply",
                    actionBody = "ua",
                    text = "\uD83C\uDDFA\uD83C\uDDE6",
                ),
                Button(
                    actionType = "reply",
                    actionBody = "en",
                    text = "\uD83C\uDDFA\uD83C\uDDF8",
                ),
                Button(
                    actionType = "reply",
                    actionBody = "ru",
                    text = "\uD83C\uDDF7\uD83C\uDDFA"
                ),
            )
        )
    )
    return welcomeMessage.toJson()
}

fun handleConversationStarted(event: ConversationStartedEvent): String {
    logger.debug { "Handle conversation started event: $event" }
    return newMessage(BotLogic().getNextUserRequest())
}


private fun newMessage(userRequest: UserRequest<*, *>): String {
    val buttons = userRequest.getOptions().map { Button(actionType = "replay", actionBody = it.key.name, text = it.value) }
    val keyboard = Keyboard(
        type = "keyboard",
        defaultHeight = false,
        inputFieldState = "hidden",
        buttons = buttons
    )
    val message = WelcomeMessage(
        sender = Sender(Constants.senderName),
        type = "text",
        text = "Доброго дня!" +
                "\nДякуємо, що звернулися до нашої служби #психологічної підтримки!" +
                "\nНаші спеціалісти готові вам надати допомогу в зручному для вас форматі, який допоможе визначити Я чат-бот." +
                "\n\nВибери мову спілкування" +
                "\nChoose language",
        trackingData = "klaxon.toJsonString(userRequest.state)",
        keyboard = keyboard
    )
    return message.toJson()
}

class Constants {
    companion object {
        const val senderName = "Чат бот"
    }
}

