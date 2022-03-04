package com.example

import com.beust.klaxon.Klaxon
import com.example.api.model.*
import com.example.api.sender.ViberApiSender
import com.example.logic.BotLogic
import com.example.logic.BotLogicState
import com.example.logic.request.UserRequest
import com.example.logic.updateState
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
    val viberApiSender = ViberApiSender()

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        get("/about") {
            call.respondText("Viber bot for psychological help")
        }
        get("/register_webhook") {
            val response = viberApiSender.registerBotWebhook()
            val stringBody: String = response.receive()
            call.respondText(response.toString() + "\n" + stringBody)
        }

        post("/webhook") {
            val body = call.receiveText()
            logger.info { "Webhook received: $call $body" }

            val parsed = klaxon.parseJsonObject(StringReader(body))
            var response = ""
            when (val event = parsed["event"]) {
                "conversation_started" -> {
                    val conversationStartedEvent = klaxon.parse<ConversationStartedEvent>(body)!!
                    response = handleConversationStarted(conversationStartedEvent)
                }
                "message" -> {
                    val clientMessageEvent = klaxon.parse<ClientMessageEvent>(body)
                    if (clientMessageEvent != null) {
                        handleClientMessage(clientMessageEvent, viberApiSender)
                    } else {
                        logger.error { "Can not parse clientMessageEvent: $body" }

                        //?????when it possible
                        TODO()
                    }
                }
                //todo handle all events
                else -> {
                    logger.warn { "Unhandled event: $event" }
                }
            }
            logger.debug { "Sending response: $response" }
            call.respond(HttpStatusCode.OK, response)
        }
    }
}

fun handleConversationStarted(event: ConversationStartedEvent): String {
    logger.debug { "Handle conversation started event: $event" }
    return newMessage(BotLogic().getNextUserRequest())
}

suspend fun handleClientMessage(event: ClientMessageEvent, viberApiSender: ViberApiSender) {
    if (event.message.trackingData != null) {
        val oldState = klaxon.parse<BotLogicState>(event.message.trackingData)!!
        val newState = updateState(oldState, newInput = event.message.text!!)
        val clientMessageRequestBody = newMessage(BotLogic(newState).getNextUserRequest(), event.sender.id)

        viberApiSender.sendMessage(clientMessageRequestBody)
    } else {
        //if not tracking data, start from the beginning
        val welcomeMessage = newMessage(BotLogic().getNextUserRequest(), event.sender.id)
        viberApiSender.sendMessage(welcomeMessage)
    }
}

private fun newMessage(userRequest: UserRequest<*, *>, receiverId: String? = null): String {
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
    val message: Any = if (keyboard != null) {
        MessageWithKeyboard(
            receiver = receiverId,
            sender = Sender(Constants.senderName),
            type = "text",
            text = userRequest.getMessage(),
            trackingData = klaxon.toJsonString(userRequest.state),
            keyboard = keyboard
        )
    } else {
        MessageWithoutKeyboard(
            receiver = receiverId,
            sender = Sender(Constants.senderName),
            type = "text",
            text = userRequest.getMessage(),
            trackingData = klaxon.toJsonString(userRequest.state)
        )
    }
    return klaxon.toJsonString(message)
}

class Constants {
    companion object {
        const val senderName = "Чат бот"
    }
}

