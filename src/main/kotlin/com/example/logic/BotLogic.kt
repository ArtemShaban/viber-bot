package com.example.logic

import com.beust.klaxon.Klaxon
import com.example.logic.request.UserRequest

abstract class BotLogic<State> protected constructor(
    protected val state: State,
    protected val userInfo: UserInfo?
) {
    abstract fun getNextUserRequest(): UserRequest<*>?
    abstract fun updateState(newInput: String)

    enum class MessengerType { VIBER, TELEGRAM }

    companion object {
        private val klaxon by lazy { Klaxon() }

        fun fromStart(type: MessengerType): BotLogic<*> {
            return when (type) {
                MessengerType.VIBER -> ViberBotLogic(null, null)
                MessengerType.TELEGRAM -> TelegramBotLogic(null, null)
            }
        }

        fun fromState(
            type: MessengerType,
            stateJsonString: String?,
            userInfo: UserInfo?
        ): BotLogic<*> {
            return when (type) {
                MessengerType.VIBER -> {
                    val state = if (stateJsonString != null) klaxon.parse<ViberBotLogic.State>(
                        stateJsonString
                    ) else null
                    ViberBotLogic(state, userInfo)
                }
                MessengerType.TELEGRAM -> {
                    val state = if (stateJsonString != null) klaxon.parse<TelegramBotLogic.State>(
                        stateJsonString
                    ) else null
                    TelegramBotLogic(state, userInfo)
                }
            }
        }
    }
}

