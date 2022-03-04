package com.example.logic.request

import com.example.logic.BotLogicState

interface UserMessage {
    fun getMessage(): String
}

abstract class UserRequest<TOption>(val state: BotLogicState) : UserMessage {
    abstract fun getOptions(): Map<TOption, String>
}

enum class Lang {
    UK, RU, EN
}