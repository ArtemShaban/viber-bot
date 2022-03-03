package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }

        get("/hi") {
            call.respondText("hi!")
        }

        get("/test") {
            call.respondText("Auto deploy is working!")
        }
    }
}
