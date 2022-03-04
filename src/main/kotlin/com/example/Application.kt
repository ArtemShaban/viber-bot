package com.example

import com.beust.klaxon.Klaxon
import com.example.api.model.*
import com.example.api.sender.ViberApiSender
import com.example.logic.BotLogic
import com.example.logic.BotLogicState
import com.example.logic.request.UserOption
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
            logger.trace { "Webhook received: $call $body" }

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
                else -> logger.warn { "Unhandled event: $event" }
            }
            logger.debug { "Sending response: $response" }
            call.respond(HttpStatusCode.OK, response)
        }
    }
}

fun handleConversationStarted(event: ConversationStartedEvent): String {
    logger.debug { "Handling conversation started event: $event" }
    val userRequest = BotLogic().getNextUserRequest()
    return if (userRequest != null) {
        newMessage(userRequest)
    } else {
        ""
    }
}

suspend fun handleClientMessage(event: ClientMessageEvent, viberApiSender: ViberApiSender) {
    logger.debug { "Handling client message event: $event" }

    val state: BotLogicState? =
        if (event.message.trackingData != null) {
            val oldState = klaxon.parse<BotLogicState>(event.message.trackingData)!!
            updateState(oldState, newInput = event.message.text!!)
        } else {
            null
        }
    val logic = if (state != null) BotLogic(state) else BotLogic()
    val userRequest = logic.getNextUserRequest()
    val messageBodyToSend =
        if (userRequest != null) newMessage(userRequest, event.sender.id) else ""

    viberApiSender.sendMessage(messageBodyToSend)
}

private fun newMessage(userRequest: UserRequest<*>, receiverId: String? = null): String {
    val buttons = if (userRequest.getOptions().isNotEmpty()) {
        userRequest.getOptions()
            .map {
                val url = (it.key as? UserOption)?.getUrl()
                Button(
                    actionType = if (url == null) "reply" else "open-url",
                    actionBody = url ?: it.key.name,
                    text = it.value,
                    silent = url != null
                )
            }
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

