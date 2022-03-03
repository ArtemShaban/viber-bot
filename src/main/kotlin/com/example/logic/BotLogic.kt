package com.example.logic

import com.example.logic.request.*

class BotLogic(private val sessionId: String) {
    private var userLang: Lang? = null
    private var userName: String? = null
    private var stateFine: Boolean? = null


    fun getNextUserRequest(prevResponse: Response<*>? = null): UserRequest<*, *> {
        if (prevResponse != null) {
            when (prevResponse.request) {
                is WelcomeRequest -> userLang = (prevResponse.responseData as Lang?)
                is EnterNameRequest -> userName = prevResponse.responseData as String?
                is CheckStateRequest -> updateUserState(prevResponse.responseData as CheckStateRequest.Option)
            }
        }
        return getNextUserRequest()
    }

    private fun updateUserState(option : CheckStateRequest.Option) {
        stateFine = option == CheckStateRequest.Option.FINE
    }

    private fun getNextUserRequest(): UserRequest<*, *> {
        return when {
            userLang == null -> WelcomeRequest()
            userName == null -> EnterNameRequest(userLang!!)
            stateFine == null -> CheckStateRequest(userLang!!)
            else -> WelcomeRequest()
        }
    }
}