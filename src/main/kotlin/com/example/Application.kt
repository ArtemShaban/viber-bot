package com.example

import com.beust.klaxon.Klaxon
import com.example.api.model.*
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

            call.respond(HttpStatusCode.OK, response)
        }
    }
}

fun handleConversationStarted(event: ConversationStartedEvent): String {
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
            listOf(
                Button(
                    actionType = "reply",
                    actionBody = "Українська",
                    text = "\uD83C\uDDFA\uD83C\uDDE6",
                    textSize = "large",
//                    image = "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/240/google/313/flag-ukraine_1f1fa-1f1e6.png"
                ),
                Button(
                    actionType = "reply",
                    actionBody = "English",
                    text = "\uD83C\uDDFA\uD83C\uDDF8",
                    textSize = "large",
//                    image = "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/240/google/313/flag-united-states_1f1fa-1f1f8.png"
                ),
                Button(
                    actionType = "reply",
                    actionBody = "Русский",
                    text = "\uD83C\uDDF7\uD83C\uDDFA",
                    textSize = "large",
//                    image = "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/240/google/313/flag-russia_1f1f7-1f1fa.png"
                ),
            )
        )
    )
    return welcomeMessage.toJson()
}
