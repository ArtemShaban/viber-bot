package com.example.api.model

import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

abstract class Event(
    val event: String
) {
    fun toJson() = klaxon.toJsonString(this)

    companion object {
        fun fromJson(json: String) = klaxon.parse<ConversationStartedEvent>(json)!!
    }
}
