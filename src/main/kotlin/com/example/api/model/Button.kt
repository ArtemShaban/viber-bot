package com.example.api.model

import com.beust.klaxon.Json

data class Button(
    @Json(name = "ActionType")
    val actionType: String,

    @Json(name = "ActionBody")
    val actionBody: String,

    @Json(name = "Text")
    val text: String,

    @Json(name = "TextSize")
    val textSize: String,

    @Json(name = "Image")
    val image: String,
)