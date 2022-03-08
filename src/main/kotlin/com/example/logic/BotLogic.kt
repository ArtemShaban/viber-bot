package com.example.logic

import com.example.api.model.User
import com.example.logic.request.*
import com.example.logic.request.ContactTypeRequest.ContactType

class BotLogic(private val state: BotLogicState = BotLogicState()) {
    private val emailLogic = EmailLogic()
    private val spreadsheetLogic = SpreadsheetLogic()

    fun getNextUserRequest(): UserRequest<*>? {
        return when {
            state.userLang == null -> WelcomeRequest(state)
            state.userName == null -> EnterNameRequest(state)
            state.state == null -> CheckStateRequest(state)
            state.stateNeedEmergencyHelp == true -> EmergencyHelpBotRequest(state)

            state.stressLevel == null -> StressLevelRequest(state)
            state.stressSource == null -> ChooseSourceRequest(state)
            state.contactType == null -> ContactTypeRequest(state)

            state.finished != null -> null
            state.phoneNumber != null -> handleFinishedState(state)

            //ask for phone number if case of PHONE_CALL and VIBER_CHAT contact types.
            ContactType.PHONE_CALL == ContactType.get(state) || ContactType.VIBER_CHAT == ContactType.get(state)
            -> EnterPhoneRequest(state)

            //show finish page for  Zoom contact types
            ContactType.ZOOM_MEETING == ContactType.get(state) -> handleFinishedState(state)

            else -> WelcomeRequest(state)
        }
    }

    private fun handleFinishedState(state: BotLogicState): FinishBotRequest {
//        emailLogic.sendEmail(state) //Send email when user finished all bot steps.
        spreadsheetLogic.addUserDataToSpreadsheet(state)
        return FinishBotRequest(state)
    }
}

fun updateState(state: BotLogicState, newInput: String, user: User): BotLogicState? {
    state.userMessengerInfo = user

    when {
        state.userLang == null -> state.userLang = newInput
        state.userName == null -> state.userName = newInput
        state.state == null -> {
            state.state = newInput
            state.stateNeedEmergencyHelp = CheckStateRequest.Option.EMERGENCY.name == state.state
        }
        state.stateNeedEmergencyHelp == true -> {
            if (EmergencyHelpBotRequest.Option.RESTART.name == newInput) return null
        }

        //todo need to validate stress level, if not int - send a message.
        state.stressLevel == null -> state.stressLevel = newInput.toInt()
        state.stressSource == null -> state.stressSource = newInput
        state.contactType == null -> state.contactType = newInput
        //remember phone number for PHONE_CALL and PHONE_CALL contact type
        state.phoneNumber == null && (ContactType.PHONE_CALL == ContactType.get(state) || ContactType.VIBER_CHAT == ContactType.get(
            state))
        -> state.phoneNumber = newInput

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
    var stateNeedEmergencyHelp: Boolean? = null,
    var stressLevel: Int? = null,
    var stressSource: String? = null,
    var contactType: String? = null,
    var phoneNumber: String? = null,
    var finished: Boolean? = null,

    var userMessengerInfo: User? = null,
)