package com.example

import io.ktor.application.*
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

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
            //todo handle webhooks here
            call.respond(HttpStatusCode.OK)
        }
    }
}
