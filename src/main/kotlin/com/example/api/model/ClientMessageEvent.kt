package com.example.api.model

import com.beust.klaxon.Json

data class ClientMessageEvent (
    val event: String,
    val timestamp: Long,
    val message: EventMessage
)

data class EventMessage (
    @Json(name = "tracking_data")
    val trackingData: String,
    val text: String
    )