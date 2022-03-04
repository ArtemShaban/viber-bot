package com.example.logic

import com.example.logic.request.*
import com.example.logic.request.ContactTypeRequest.ContactType

class BotLogic(private val state: BotLogicState = BotLogicState()) {

    fun getNextUserRequest(): UserRequest<*>? {
        return when {
            state.userLang == null -> WelcomeRequest(state)
            state.userName == null -> EnterNameRequest(state)
            state.state == null -> CheckStateRequest(state)
            state.stateFine == null -> null
            state.stressLevel == null -> RateLevelRequest(state)
            state.stressSource == null -> ChooseSourceRequest(state)
            state.contactType == null -> ContactTypeRequest(state)

            state.finished != null -> null
            state.phoneNumber != null -> FinishBotRequest(state)

            //ask for phone number
            ContactType.PHONE_CALL == ContactType.get(state) -> EnterPhoneRequest(state)

            //show finish page for Viber and Zoom contact types
            ContactType.VIBER_CHAT == ContactType.get(state) || ContactType.ZOOM_MEETING == ContactType.get(state) ->
                FinishBotRequest(state)

            else -> WelcomeRequest(state)
        }
    }
}

fun updateState(state: BotLogicState, newInput: String): BotLogicState? {
    when {
        state.userLang == null -> state.userLang = newInput
        state.userName == null -> state.userName = newInput
        state.stateFine == null -> {
            state.state = newInput
            state.stateFine = if (CheckStateRequest.Option.FINE.name == state.state) true else null
        }
        state.stressLevel == null -> state.stressLevel =
            newInput.toInt() //todo need to validate, if not int - send a message.
        state.stressSource == null -> state.stressSource = newInput
        state.contactType == null -> state.contactType = newInput
        ContactType.get(state) == ContactType.PHONE_CALL && state.phoneNumber == null -> state.phoneNumber =
            newInput
        state.finished == null -> {
            state.finished = true
            EmailLogic().sendEmail(state) //Send email when user finished all bot steps.
        }

        FinishBotRequest.Option.RESTART.name == newInput -> return null
    }
    return state
}

data class BotLogicState(
    var userLang: String? = null,
    var userName: String? = null,
    var state: String? = null,
    var stateFine: Boolean? = null,
    var stressLevel: Int? = null,
    var stressSource: String? = null,
    var contactType: String? = null,
    var phoneNumber: String? = null,
    var finished: Boolean? = null,
)