package com.example.logic

import com.example.api.model.User
import com.example.logic.BotLogic.MessengerType

class UserInfo {
    val id: String
    val name: String
    val avatar: String?
    val country: String?
    val language: String?
    val messenger: MessengerType

    constructor(user: User) {
        this.id = user.id
        this.name = user.name
        this.avatar = user.avatar
        this.country = user.country
        this.language = user.language
        this.messenger = MessengerType.VIBER
    }

    constructor(user: com.github.kotlintelegrambot.entities.User) {
        this.id = user.id.toString()
        this.name = "${user.firstName} ${user.lastName} (${user.username})"
        this.avatar = null
        this.country = null
        this.language = user.languageCode
        this.messenger = MessengerType.TELEGRAM
    }
}