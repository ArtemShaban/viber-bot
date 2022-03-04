package com.example.logic

import com.example.logic.request.CheckStateRequest
import com.example.logic.request.EnterNameRequest
import com.example.logic.request.UserRequest
import com.example.logic.request.WelcomeRequest

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

fun processState(state: BotLogicState, text: String): BotLogicState {
    when {
        state.userLang == null -> state.userLang = text
        state.userName == null -> state.userName = text
        state.stateFine == null -> state.stateFine = true //TODO identify boolean value and make logic for emergency call
    }
    return state
}

data class BotLogicState(
    var userLang: String? = null,
    var userName: String? = null,
    var stateFine: Boolean? = null
)