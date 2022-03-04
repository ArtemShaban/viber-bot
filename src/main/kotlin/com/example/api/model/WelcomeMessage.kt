package com.example.api.model

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

data class WelcomeMessage(
    val receiver: String? = null,
    val sender: Sender,

    val type: String, //text, picture, video, file, location, contact, sticker, carousel content, url
    val text: String,

    @Json(name = "tracking_data")
    val trackingData: String,

    val keyboard: Keyboard,
    @Json(name = "min_api_version")
    val minApiVersion: Int = 4
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<WelcomeMessage>(json)
    }
}

data class MessageWithoutKeyboard(
    val receiver: String? = null,
    val sender: Sender,

    val type: String, //text, picture, video, file, location, contact, sticker, carousel content, url
    val text: String,

    @Json(name = "tracking_data")
    val trackingData: String,

    @Json(name = "min_api_version")
    val minApiVersion: Int = 4
)

