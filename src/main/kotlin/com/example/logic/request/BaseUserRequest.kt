package com.example.logic.request

interface UserMessage {
    fun getMessage(): String
}

interface UserRequest<TOption : Enum<TOption>, TResult> : UserMessage {
    fun getOptions(): Map<TOption, String>
    fun createResponse(responseData: TResult): Response<TResult>
}

data class Response<T> (val request: UserRequest<*, T>, val responseData: T)

enum class Lang {
    UK, RU, EN
}