package com.example.api.model

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon

private val klaxon = Klaxon()

data class WelcomeMessage(
    val sender: Sender,

    val type: String, //text, picture, video, file, location, contact, sticker, carousel content, url
    val text: String,

    @Json(name = "tracking_data")
    val trackingData: String,

    val keyboard: Keyboard,
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<WelcomeMessage>(json)
    }
}


