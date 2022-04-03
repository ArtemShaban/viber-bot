package com.example.api.model

import com.beust.klaxon.Json

data class Keyboard(
    @Json(name = "Type")
    val type: String,

    @Json(name = "DefaultHeight")
    val defaultHeight: Boolean,

    @Json(name = "CustomDefaultHeight")
    val customDefaultHeight: Int,

    @Json(name = "InputFieldState")
    val inputFieldState: String,

    @Json(name = "Buttons")
    val buttons: List<Button>
)