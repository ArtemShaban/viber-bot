package com.example.logic.request.viber

import com.example.logic.ViberBotLogic
import com.example.logic.request.Lang
import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class EnterPhoneRequest(override val state: ViberBotLogic.State) :
    UserRequest<EnterPhoneRequest.Option>(state) {
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