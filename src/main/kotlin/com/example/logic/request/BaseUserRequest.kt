package com.example.logic.request

import com.example.logic.BotLogicState

interface UserMessage {
    fun getMessage(): String
}

abstract class UserRequest<TOption>(val state: BotLogicState) :
    UserMessage where TOption : Enum<TOption>, TOption : UserOption {
    abstract fun getOptions(): Map<TOption, String>
}

interface UserOption {
    fun getUrl(): String? = null
}

enum class Lang : UserOption {
    UK, RU, EN
}