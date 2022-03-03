package com.example.api.model

data class ClientMessageEvent (
    val event: String,
    val timestamp: Long,
    val message: EventMessage
)

data class EventMessage (val tracking_data: String)