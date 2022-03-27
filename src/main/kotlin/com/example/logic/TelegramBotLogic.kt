package com.example.logic

import com.example.logic.request.UserRequest
import com.example.logic.request.telegram.ContactsInfoRequest
import com.example.logic.request.telegram.EnterNameRequest
import com.example.logic.request.telegram.EnterPhoneRequest
import com.example.logic.request.telegram.IntroMessageRequest

class TelegramBotLogic internal constructor(state: State?, userInfo: UserInfo?) :
    BotLogic<TelegramBotLogic.State>(state ?: State(), userInfo) {
    data class State(
        var introContinued: Boolean? = null,
        var userName: String? = null,
        var phoneNumber: String? = null,
    )

    override fun getNextUserRequest(): UserRequest<*>? {
        return when {
            state.introContinued == null -> IntroMessageRequest(state)
            state.userName == null -> EnterNameRequest(state)
            state.phoneNumber == null -> EnterPhoneRequest(state)
            else -> handleLastState()
        }
    }

    private fun handleLastState(): ContactsInfoRequest {
        SpreadsheetLogic().addTelegramUserDataToSpreadsheet(state, userInfo)
        return ContactsInfoRequest(state)
    }

    override fun updateState(newInput: String) {
        when {
            state.introContinued == null && IntroMessageRequest.Option.valueOf(newInput) == IntroMessageRequest.Option.CONTINUE ->
                state.introContinued = true
            state.userName == null -> state.userName = newInput
            state.phoneNumber == null -> state.phoneNumber = newInput
        }
    }
}