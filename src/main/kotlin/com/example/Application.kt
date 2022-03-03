package com.example

import com.beust.klaxon.Klaxon
import com.example.api.model.*
import com.example.logic.BotLogic
import com.example.logic.BotLogicState
import com.example.logic.processState
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
                "message" -> {
                    val message = klaxon.parse<ClientMessageEvent>(StringReader(body))
                    if (message != null) {
                        response = handleClientMessage(message)
                    } else {
                        TODO()
                    }
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

fun handleClientMessage(event :ClientMessageEvent): String {
    var response = ""
    if (event.message.trackingData!= null) {
        val state = processState(klaxon.parse<BotLogicState>(StringReader(event.message.trackingData))!!, event.message.text!!)
        response = newMessage(BotLogic(state).getNextUserRequest())
    }
    return response
}

fun handleConversationStarted(event: ConversationStartedEvent): String {
    logger.debug { "Handle conversation started event: $event" }
    return newMessage(BotLogic().getNextUserRequest())
}


private fun newMessage(userRequest: UserRequest<*, *>): String {
    val buttons = if (userRequest.getOptions().isNotEmpty()) {
        userRequest.getOptions()
            .map { Button(actionType = "reply", actionBody = it.key.name, text = it.value) }
    } else {
        null
    }
    val keyboard = if (buttons != null) {
        Keyboard(
            type = "keyboard",
            defaultHeight = false,
            inputFieldState = "hidden",
            buttons = buttons
        )
    } else {
        null
    }
    val message = WelcomeMessage(
        sender = Sender(Constants.senderName),
        type = "text",
        text = userRequest.getMessage(),
        trackingData = klaxon.toJsonString(userRequest.state),
        keyboard = keyboard
    )
    return message.toJson()
}

class Constants {
    companion object {
        const val senderName = "Чат бот"
    }
}

