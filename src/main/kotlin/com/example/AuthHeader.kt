package com.example

fun getAuthHeader(): String {
    val token = System.getenv("VIBER_TOKEN") ?: throw RuntimeException("VIBER_TOKEN env variable it not specified")
    return "X-Viber-Auth-Token: $token"
}