package com.example.api.model

import com.beust.klaxon.Json

data class User(
    val id: String,
    val name: String,
    val avatar: String,
    val country: String,
    val language: String,

    @Json(name = "api_version")
    val apiVersion: Long
)