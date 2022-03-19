package com.example

import com.beust.klaxon.Klaxon
import com.example.api.model.*
import com.example.api.sender.ViberApiSender
import com.example.logic.BotLogic
import com.example.logic.BotLogicState
import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest
import com.example.logic.updateState
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.network.fold
import com.github.kotlintelegrambot.webhook
import io.ktor.application.*
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import mu.KotlinLogging
import java.io.StringReader
import java.net.URLDecoder
import java.net.URLEncoder

private const val SERVER_HOSTNAME = "https://viber-bot-ua-help.herokuapp.com"
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
        get("/bot") {
            val resource = this::class.java.classLoader.getResource("bot.html")!!.readText(Charsets.UTF_8)
            call.respondText(resource, ContentType.Text.Html)
        }
        /*-----------------------------------------------------------*/
        //bind viber handling
        val viberApiSender = ViberApiSender(SERVER_HOSTNAME)

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

        /*-----------------------------------------------------------*/
        //bind tg handling
        val tgBot = initTgBot()
        tgBot.startWebhook()

        //todo think maybe we can use another url?
        post("/${getTgBotToken()}") {
            val response = call.receiveText()
            tgBot.processUpdate(response)
            call.respond(HttpStatusCode.OK)
        }
    }

}

private fun initTgBot() = bot {
    val tgBotToken = getTgBotToken()
    token = tgBotToken

    webhook {
        url = "${SERVER_HOSTNAME}/${tgBotToken}"
        /* This certificate argument is only needed when you want Telegram to trust your
        * self-signed certificates. If you have a CA trusted certificate you can omit it.
        * More info -> https://core.telegram.org/bots/webhooks */
//        certificate = TelegramFile.ByFile(File(CertificateUtils.certPath))
//        maxConnections = 50
        allowedUpdates = listOf("message", "callback_query")
    }

    dispatch {
        command("start") {
            logger.debug { "tg new start command" }
            tgSendNewRequest(bot, message.chat.id, BotLogic().getNextUserRequest(), update)

        }
        text {
            logger.debug { "tg new message with text $text and reply to message ${message.replyToMessage}" }
            if (message.replyToMessage != null) {
                val request = tgGetNextRequest(message.replyToMessage!!, text)
                tgSendNewRequest(bot, message.replyToMessage!!.chat.id, request, update)
            } else {
                tgSendNewRequest(bot, message.chat.id, BotLogic().getNextUserRequest(), update)
            }
        }
        callbackQuery {
            logger.debug { "tg new callback query with data ${callbackQuery.data} and reply to message:${callbackQuery.message}" }
            if (callbackQuery.message != null) {
                val request = tgGetNextRequest(callbackQuery.message!!, callbackQuery.data)
                tgSendNewRequest(bot, callbackQuery.message!!.chat.id, request, update)
            }
        }
    }
}

fun tgSendNewRequest(bot: Bot, chatId: Long, request: UserRequest<*>?, update: Update) {
    if (request != null) {
        val encodedState = URLEncoder.encode(klaxon.toJsonString(request.state), "utf-8")
        val text = "${request.getMessage()}<a href=\"tg://btn/$encodedState\">\u200b</a>"
        val replyMarkup = if (request.getOptions().isNotEmpty()) {
            InlineKeyboardMarkup.create(
                request.getOptions().map {
                    val url = (it.key as? UserOption)?.getUrl()
                    listOf(
                        if (url != null) InlineKeyboardButton.Url(
                            it.value,
                            url
                        ) else InlineKeyboardButton.CallbackData(it.value, it.key.name)
                    )
                })
        } else {
            ForceReplyMarkup(true)
        }
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = text,
            parseMode = ParseMode.HTML,
            replyMarkup = replyMarkup
        ).fold({
            logger.debug { "tg send message response is $it" }
        }, {
            logger.error { "tg send message error ${it.errorBody}" }
        })
        update.consume()
    }
}

fun tgGetNextRequest(message: Message, data: String): UserRequest<*>? {
    val stateString =
        message.entities?.find { it.type == MessageEntity.Type.TEXT_LINK }?.url?.substringAfter("tg://btn/")
    val state = if (stateString != null) klaxon.parse<BotLogicState>(
        URLDecoder.decode(stateString, "utf-8")
    )?.let {
        updateState(it, data, null)
    } else null
    val logic = if (state != null) BotLogic(state) else BotLogic()
    return logic.getNextUserRequest()
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
            updateState(oldState, newInput = event.message.text!!, event.sender)
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

private fun getTgBotToken(): String {
    return System.getenv("TG_TOKEN") ?: throw RuntimeException("TG_TOKEN env variable it not specified")
}

class Constants {
    companion object {
        const val senderName = "Чат бот"
    }
}

