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
            ContactTypeRequest.Option.getContactType(state) == ContactTypeRequest.Option.PHONE_CALL -> EnterPhoneRequest(
                state
            )
            state.phoneNumber != null -> FinishBotRequest(state)
            ContactTypeRequest.Option.getContactType(state) == ContactTypeRequest.Option.VIBER_CHAT
                    || ContactTypeRequest.Option.getContactType(state) == ContactTypeRequest.Option.ZOOM_MEETING -> FinishBotRequest(
                state
            )
            else -> WelcomeRequest(state)
        }
    }


}

fun updateState(state: BotLogicState, newInput: String): BotLogicState {
    when {
        state.userLang == null -> state.userLang = newInput
        state.userName == null -> state.userName = newInput
        state.stateFine == null -> state.stateFine = true//TODO
        state.stressLevel == null -> state.stressLevel =
            newInput.toInt() //todo need to validate, if not int - send a message.
        state.stressSource == null -> state.stressSource = newInput
        state.contactType == null -> state.contactType = newInput
        state.phoneNumber == null -> state.phoneNumber = newInput
    }
    return state
}

data class BotLogicState(
    var userLang: String? = null,
    var userName: String? = null,
    var stateFine: Boolean? = null,
    var stressLevel: Int? = null,
    var stressSource: String? = null,
    var contactType: String? = null,
    var phoneNumber: String? = null,
)