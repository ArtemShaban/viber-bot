package com.example.logic

import com.example.api.model.User
import com.example.logic.request.*
import com.example.logic.request.CheckStateRequest.UserState

class BotLogic(private val state: BotLogicState = BotLogicState()) {
    private val emailLogic = EmailLogic()
    private val spreadsheetLogic = SpreadsheetLogic()

    fun getNextUserRequest(): UserRequest<*>? {
        return when {
            state.userLang == null -> WelcomeRequest(state)
            state.userName == null -> EnterNameRequest(state)
            state.phoneNumber == null -> EnterPhoneRequest(state)
            state.state == null -> CheckStateRequest(state)
            state.emergencyHelpShown != null -> null
            UserState.EMERGENCY == UserState.get(state) -> EmergencyHelpBotRequest(state)

            state.stressLevel == null -> StressLevelRequest(state)
            state.stressSource == null -> ChooseSourceRequest(state)
            state.contactType == null -> ContactTypeRequest(state)
            state.finished == null -> handleFinishedState(state)
            state.finished != null -> null

            else -> WelcomeRequest(state)
        }
    }

    private fun handleFinishedState(state: BotLogicState): FinishBotRequest {
//        emailLogic.sendEmail(state) //Send email when user finished all bot steps.
        if (state.userMessengerInfo != null) {
            SpreadsheetLogic().addUserDataToSpreadsheet(state)
        }
        return FinishBotRequest(state)
    }
}

fun updateState(state: BotLogicState, newInput: String, user: User?): BotLogicState? {
    state.userMessengerInfo = user

    when {
        state.userLang == null -> state.userLang = newInput
        state.userName == null -> state.userName = newInput
        state.phoneNumber == null -> state.phoneNumber = newInput
        state.state == null -> state.state = newInput
        UserState.EMERGENCY == UserState.get(state) -> {
            state.emergencyHelpShown = true
            if (EmergencyHelpBotRequest.Option.RESTART.name == newInput) return null
        }

        //todo need to validate stress level, if not int - send a message.
        state.stressLevel == null -> state.stressLevel = newInput.toInt()
        state.stressSource == null -> state.stressSource = newInput
        state.contactType == null -> state.contactType = newInput

        state.finished == null -> {
            state.finished = true
            if (FinishBotRequest.Option.RESTART.name == newInput) return null
        }
    }
    return state
}

data class BotLogicState(
    var userLang: String? = null,
    var userName: String? = null,
    var state: String? = null,
    var emergencyHelpShown: Boolean? = null,
    var stressLevel: Int? = null,
    var stressSource: String? = null,
    var contactType: String? = null,
    var phoneNumber: String? = null,
    var finished: Boolean? = null,

    var userMessengerInfo: User? = null,
)