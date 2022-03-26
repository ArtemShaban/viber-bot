package com.example.logic.request.telegram

import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class EnterNameRequest(state: Any) : UserRequest<EnterNameRequest.Option>(state) {
    enum class Option : UserOption

    override fun getMessage() = "Як до вас звертатися?"

    override fun getOptions(): Map<Option, String> = emptyMap()
}