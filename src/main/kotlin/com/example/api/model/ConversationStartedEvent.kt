package com.example.api.model

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

data class ConversationStartedEvent(
    val event: String,
    val timestamp: Long,

    @Json(name = "message_token")
    val messageToken: Long,

    val type: String,
    val context: String = "", //context is optional field
    val user: User,
    val subscribed: Boolean
) {
    fun toJson() = klaxon.toJsonString(this)

    companion object {
        fun fromJson(json: String) = klaxon.parse<ConversationStartedEvent>(json)
    }
}

