package com.example.logic

import com.example.logic.request.*

class BotLogic(private val state: BotLogicState = BotLogicState()) {

    fun getNextUserRequest(): UserRequest<*, *> {
        return when {
            state.userLang == null -> WelcomeRequest(state)
            state.userName == null -> EnterNameRequest(state)
            state.stateFine == null -> CheckStateRequest(state)
            else -> WelcomeRequest(state)
        }
    }
}

data class BotLogicState(
    var userLang: String? = null,
    var userName: String? = null,
    var stateFine: Boolean? = null
)