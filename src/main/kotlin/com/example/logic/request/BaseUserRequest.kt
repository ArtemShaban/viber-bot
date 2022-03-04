package com.example.logic.request

import com.example.logic.BotLogicState

interface UserMessage {
    fun getMessage(): String
}

abstract class UserRequest<TOption : Enum<TOption>, TResult>(val state: BotLogicState) : UserMessage {
    abstract fun getOptions(): Map<TOption, String>
    abstract fun createResponse(responseData: TResult): Response<TResult>
}

data class Response<T>(val request: UserRequest<*, T>, val responseData: T)

enum class Lang {
    UK, RU, EN
}