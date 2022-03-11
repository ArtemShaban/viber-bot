package com.example.logic.request

import com.example.logic.BotLogicState

class EnterPhoneRequest(state: BotLogicState) : UserRequest<EnterPhoneRequest.Option>(state) {
    enum class Option : UserOption

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Ваш контактний номер для зв'язку"
            Lang.RU -> "Ваш контактный номер для связи"
            Lang.EN -> "Your active mobile number"
        }
    }

    override fun getOptions(): Map<Option, String> {
        return emptyMap()
    }
}