package com.example.logic

import com.example.logic.request.UserRequest
import com.example.logic.request.viber.*

class ViberBotLogic internal constructor(state: State?, userInfo: UserInfo?) :
    BotLogic<ViberBotLogic.State>(state ?: State(), userInfo) {
    data class State(
        var userLang: String? = null,
        var userName: String? = null,
        var state: String? = null,
        var emergencyHelpShown: Boolean? = null,
        var stressLevel: Int? = null,
        var stressSource: String? = null,
        var contactType: String? = null,
        var phoneNumber: String? = null,
        var finished: Boolean? = null,
    )

    override fun getNextUserRequest(): UserRequest<*>? {
        return when {
            state.userLang == null -> WelcomeRequest(state)
            state.userName == null -> EnterNameRequest(state)
            state.phoneNumber == null -> EnterPhoneRequest(state)
            state.state == null -> CheckStateRequest(state)
            state.emergencyHelpShown != null -> null
            CheckStateRequest.UserState.EMERGENCY == CheckStateRequest.UserState.get(state) -> EmergencyHelpBotRequest(
                state
            )

            state.stressLevel == null -> StressLevelRequest(state)
            state.stressSource == null -> ChooseSourceRequest(state)
            state.contactType == null -> ContactTypeRequest(state)
            state.finished == null -> handleFinishedState(state)
            state.finished != null -> null

            else -> WelcomeRequest(state)
        }
    }

    private fun handleFinishedState(state: State): FinishBotRequest {
//        emailLogic.sendEmail(state) //Send email when user finished all bot steps.
        SpreadsheetLogic().addViberUserDataToSpreadsheet(state, userInfo)
        return FinishBotRequest(state)
    }

    override fun updateState(newInput: String) {
        when {
            state.userLang == null -> state.userLang = newInput
            state.userName == null -> state.userName = newInput
            state.phoneNumber == null -> state.phoneNumber = newInput
            state.state == null -> state.state = newInput
            CheckStateRequest.UserState.EMERGENCY == CheckStateRequest.UserState.get(state) -> {
                state.emergencyHelpShown = true
                if (EmergencyHelpBotRequest.Option.RESTART.name == newInput) resetState()
            }

            //todo need to validate stress level, if not int - send a message.
            state.stressLevel == null -> state.stressLevel = newInput.toInt()
            state.stressSource == null -> state.stressSource = newInput
            state.contactType == null -> state.contactType = newInput

            state.finished == null -> {
                state.finished = true
                if (FinishBotRequest.Option.RESTART.name == newInput) resetState()
            }
        }
    }

    private fun resetState() {
        state.userLang = null
        state.userName = null
        state.state = null
        state.emergencyHelpShown = null
        state.stressLevel = null
        state.stressSource = null
        state.contactType = null
        state.phoneNumber = null
        state.finished = null
    }
}