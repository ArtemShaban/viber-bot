package com.example.logic.request.viber

import com.example.logic.ViberBotLogic
import com.example.logic.request.Lang
import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class EnterNameRequest(override val state: ViberBotLogic.State) :
    UserRequest<EnterNameRequest.Option>(state) {

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Як я можу до вас звертатись?"
            Lang.RU -> "Как я могу к вам обращаться?"
            Lang.EN -> "Please write your name to continue"
        }
    }

    override fun getOptions(): Map<Option, String> {
        return emptyMap()
    }

    enum class Option : UserOption
}