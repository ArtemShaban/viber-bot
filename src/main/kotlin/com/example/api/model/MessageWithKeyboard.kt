package com.example.api.model

import com.beust.klaxon.Json

data class MessageWithKeyboard(
    val receiver: String? = null, //optional ONLY for Welcome message
    val sender: Sender,

    val type: String, //text, picture, video, file, location, contact, sticker, carousel content, url
    val text: String,

    @Json(name = "tracking_data")
    val trackingData: String,

    val keyboard: Keyboard,

    @Json(name = "min_api_version")
    val minApiVersion: Int = 4
)

