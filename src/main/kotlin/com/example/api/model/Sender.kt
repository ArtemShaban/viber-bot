package com.example.api.model

data class Sender(
    val name: String,
    val avatar: String? = null, //optional
    val id: String? = null
)