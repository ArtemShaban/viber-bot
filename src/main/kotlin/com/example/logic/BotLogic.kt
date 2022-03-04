package com.example.logic

import com.example.logic.request.*

class BotLogic(private val state: BotLogicState = BotLogicState()) {

    fun getNextUserRequest(): UserRequest<*> {
        return when {
            state.userLang == null -> WelcomeRequest(state)
            state.userName == null -> EnterNameRequest(state)
            state.stateFine == null -> CheckStateRequest(state)
            state.stressLevel == null -> RateLevelRequest(state)
            state.stressSource == null -> ChooseSourceRequest(state)
            state.contactType == null -> ContactTypeRequest(state)
            else -> WelcomeRequest(state)
        }
    }
}

fun updateState(state: BotLogicState, newInput: String): BotLogicState {
    when {
        state.userLang == null -> state.userLang = newInput
        state.userName == null -> state.userName = newInput
        state.stateFine == null -> state.stateFine = true//TODO
        state.stressLevel == null -> state.stressLevel = newInput.toInt()
        state.stressSource == null -> state.stressSource = newInput
        state.contactType == null -> state.contactType = newInput
    }
    return state
}

data class BotLogicState(
    var userLang: String? = null,
    var userName: String? = null,
    var stateFine: Boolean? = null,
    var stressLevel: Int? = null,
    var stressSource: String? = null,
    var contactType: String? = null
)