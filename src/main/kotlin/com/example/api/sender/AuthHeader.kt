package com.example.api.sender

fun getAutHeaderName(): String {
    return "X-Viber-Auth-Token"
}

fun getAuthToken(): String {
    return System.getenv("VIBER_TOKEN") ?: throw RuntimeException("VIBER_TOKEN env variable it not specified")
}